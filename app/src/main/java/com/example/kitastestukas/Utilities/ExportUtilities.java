package com.example.kitastestukas.Utilities;

import static android.os.Environment.DIRECTORY_DOCUMENTS;

import android.content.Context;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Log;

import com.example.kitastestukas.Models.Payment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ExportUtilities {
    public static void writeStringAsFile(List<Payment> paymentList, String fileName, Context context) {
        try {
            FileWriter out = new FileWriter(new File(context.getExternalFilesDir(DIRECTORY_DOCUMENTS), fileName));
            for (Payment payment : paymentList) {
                out.write(payment.toString());
            }
            out.close();
        } catch (IOException e) {
            Log.e("error", e.toString());
        }
    }
}
