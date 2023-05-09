package com.subhajit0061.notes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.subhajit0061.notes.databinding.ActivityHomeBinding
import com.subhajit0061.notes.functions.NotesDetails

class Home : AppCompatActivity() {
    companion object{
        val list = ArrayList<NotesDetails>()
    }
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAdd.setOnClickListener {
            val intent = Intent(this, Add::class.java)
            startActivity(intent)
        }
    }
}