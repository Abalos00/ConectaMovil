package com.example.conectamvil;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemDecoration extends RecyclerView.ItemDecoration {

    private final int espacio;

    public ItemDecoration(int espacio) {
        this.espacio = espacio;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.left = espacio;
        outRect.right = espacio;
        outRect.bottom = espacio;

        // Agrega un margen superior solo para el primer elemento para evitar un espacio doble entre elementos
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = espacio;
        } else {
            outRect.top = 0;
        }
    }
}

