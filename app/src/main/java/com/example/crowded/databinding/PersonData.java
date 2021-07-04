package com.example.crowded.databinding;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.time.LocalDateTime;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.library.baseAdapters.BR;

public class PersonData extends BaseObservable {
    String nombres;
    String apellidos;
    LocalDateTime fechaNacimiento;
    String correoElectronico;
    String imagenPerfil;
    String telefono;

    public PersonData() {
    }

    @Bindable
    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
        notifyPropertyChanged(BR.nombres);
    }

    @Bindable
    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
        notifyPropertyChanged(BR.apellidos);
    }

    @Bindable
    public LocalDateTime getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDateTime fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
        notifyPropertyChanged(BR.fechaNacimiento);
    }

    @Bindable
    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
        notifyPropertyChanged(BR.correoElectronico);
    }

    @Bindable
    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
        notifyPropertyChanged(BR.telefono);
    }

    @Bindable
    public String getImagenPerfil() {
        return imagenPerfil;
    }

    public void setImagenPerfil(String imagenPerfil) {
        this.imagenPerfil = imagenPerfil;
        notifyPropertyChanged(BR.imagenPerfil);
    }

    @BindingAdapter({"imagenPerfil"})
    public static void loadImage(ImageView view, String imageUrl) {
        Glide.with(view.getContext())
                .load(imageUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(view);
    }
}
