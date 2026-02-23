package com.aipasa.firebase;

import okhttp3.OkHttpClient;

public class SupabaseClient {

    public static final String SUPABASE_URL =
            "https://gmaozqdfsfqwhanvcauw.supabase.co";

    public static final String SUPABASE_KEY =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImdtYW96cWRmc2Zxd2hhbnZjYXV3Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzE0NDUzOTMsImV4cCI6MjA4NzAyMTM5M30.nfrEUY9BSH5A-XODRi9KSHke30MLf2_HlOSGQVZqI8w";

    public static final String BUCKET_NAME = "mascotas";

    private static final OkHttpClient client = new OkHttpClient();

    public static OkHttpClient getClient() {
        return client;
    }
}