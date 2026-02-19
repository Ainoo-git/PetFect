package com.aipasa.supabase

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage

object SupabaseManager {

    val client: SupabaseClient = createSupabaseClient(
        supabaseUrl = "https://gmaozqdfsfqhanvcauw.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImdtYW96cWRmc2Zxd2hhbnZjYXV3Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzE0NDUzOTMsImV4cCI6MjA4NzAyMTM5M30.nfrEUY9BSH5A-XODRi9KSHke30MLf2_HlOSGQVZqI8w"
    ) {
        install(Postgrest)
        install(Storage)
    }
}
