package com.pedromoreirareisgmail.pchat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.UploadTask;
import com.pedromoreirareisgmail.pchat.databinding.ActivitySettingsBinding;
import com.pedromoreirareisgmail.pchat.fire.Fire;
import com.pedromoreirareisgmail.pchat.fire.FireUtils;
import com.pedromoreirareisgmail.pchat.models.Usuario;
import com.pedromoreirareisgmail.pchat.utils.Comprimir;
import com.pedromoreirareisgmail.pchat.utils.Const;
import com.pedromoreirareisgmail.pchat.utils.Internet;
import com.pedromoreirareisgmail.pchat.utils.PicassoDownload;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivitySettingsBinding mBinding;
    private DatabaseReference mRefUsuario;
    private CircleImageView mCivImagem;
    private TextView mTvNome;
    private TextView mTvStatus;
    private ProgressDialog mDialog;
    private Context mContext;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_settings);

        initViews();
    }

    private void initViews() {

        mContext = SettingsActivity.this;

        usuario = new Usuario();

        Toolbar toolbar = (Toolbar) mBinding.toolbarSettings;
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.titulo_settings));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCivImagem = mBinding.civSettingsImagem;
        mTvNome = mBinding.tvSettingsNome;
        mTvStatus = mBinding.tvSettingsStatus;

        mBinding.butSettingsImagem.setOnClickListener(this);
        mBinding.butSettingsStatus.setOnClickListener(this);

        mDialog = new ProgressDialog(mContext);
    }

    @Override
    protected void onStart() {
        super.onStart();

        initFirebase();

        if (Fire.getUsuario() != null) {

            FireUtils.usuarioOnLine(mRefUsuario);
        }
    }

    private void initFirebase() {

        mRefUsuario = Fire.getRefUsuario();
        mRefUsuario.keepSynced(true);

        recuperarDadosDoUsuario();
    }

    private void recuperarDadosDoUsuario() {

        mRefUsuario.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                usuario = Objects.requireNonNull(dataSnapshot.getValue(Usuario.class));

                mTvNome.setText(usuario.getNome());
                mTvStatus.setText(usuario.getStatus());

                if (!usuario.getUrlImagem().equals(Const.IMG_PADRAO_IMAGEM)) {

                    PicassoDownload.civChachePlaceholder(mContext, usuario.getUrlImagem(), R.drawable.ic_usuario, mCivImagem);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.but_settings_imagem:

                if (Internet.temInternet(mContext)) {

                    CropImage.activity().setGuidelines(CropImageView.Guidelines.ON)
                            .setCropShape(CropImageView.CropShape.OVAL)
                            .setAspectRatio(1, 1)
                            .start(this);
                } else {

                    return;
                }
                break;

            case R.id.but_settings_status:

                Intent statusIntent = new Intent(mContext, StatusActivity.class);
                statusIntent.putExtra(Const.INTENT_SETTINGS_STATUS,usuario);
                startActivity(statusIntent);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                Uri resultUri = result.getUri();

                prepararImagemPararSalvar(resultUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
                error.printStackTrace();
            }
        }
    }

    private void prepararImagemPararSalvar(Uri resultUri) {

        File filePath = new File(resultUri.getPath());

        final byte[] imagemPerfilByte = Comprimir.comprimirImagem(mContext, filePath, 300, 300, 40);
        final byte[] imagemThumbByte = Comprimir.comprimirImagem(mContext, filePath, 200, 200, 10);

        salvarImagem(imagemPerfilByte, imagemThumbByte);
    }

    private void salvarImagem(byte[] imagemPerfilByte, final byte[] imagemThumbByte) {

        mDialog.setTitle(getString(R.string.dialog_titulo_salvando_alteracoes));
        mDialog.setMessage(getString(R.string.dialog_msg_por_favor_aguarde));
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);
        mDialog.show();

        Fire.getStorageRefImgPerfil().putBytes(imagemPerfilByte).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                if (task.isSuccessful()) {

                    final String urlDownloadPerfil = Objects.requireNonNull(task.getResult().getDownloadUrl()).toString();

                    UploadTask uploadTask = Fire.getStorageRefImgThumbnail().putBytes(imagemThumbByte);

                    uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> taskThumnail) {

                            if (taskThumnail.isSuccessful()) {

                                String urlDownloadThumb = Objects.requireNonNull(taskThumnail.getResult().getDownloadUrl()).toString();

                                Map<String, Object> dadosUrlImagem = new HashMap<>();
                                dadosUrlImagem.put(Const.USUARIO_IMAGEM, urlDownloadPerfil);
                                dadosUrlImagem.put(Const.USUARIO_URLTHUMBNAIL, urlDownloadThumb);

                                mRefUsuario.updateChildren(dadosUrlImagem);

                                PicassoDownload.civChachePlaceholder(mContext, urlDownloadPerfil, R.drawable.ic_usuario, mCivImagem);

                                mDialog.dismiss();
                            }
                        }
                    });

                } else {

                    mDialog.dismiss();
                    Toast.makeText(mContext, getString(R.string.toast_alteracoes_erro), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();

        FireUtils.ultimoAcesso(mRefUsuario);
    }
}
