package com.hugo.myapplication.utils;

import android.text.Editable;
import android.text.TextWatcher;

public class MaskWatcher implements TextWatcher {
    private boolean isUpdating;
    private final String mask = "##/##/####";
    private String oldText = "";

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable editable) {
        if (isUpdating) {
            isUpdating = false;
            return;
        }

        String clean = editable.toString().replaceAll("[^0-9]", ""); // Remove tudo que não for número
        StringBuilder formatted = new StringBuilder();
        int i = 0;

        for (char m : mask.toCharArray()) {
            if (m != '#' && clean.length() > oldText.length()) {
                formatted.append(m);
                continue;
            }
            try {
                formatted.append(clean.charAt(i));
            } catch (Exception e) {
                break;
            }
            i++;
        }

        isUpdating = true;
        oldText = formatted.toString();
        editable.replace(0, editable.length(), formatted.toString()); // Substitui o texto no campo
    }

    public static MaskWatcher buildDateMask() {
        return new MaskWatcher();
    }
}
