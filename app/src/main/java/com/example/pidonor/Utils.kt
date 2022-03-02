package com.example.pidonor

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.example.pidonor.model.Person
import com.example.pidonor.views.AddBloodGroupInfoFragment
import com.google.android.material.textfield.TextInputLayout
import java.lang.Exception

class Utils {
    companion object {
        private const val TAG = "Utils"
        const val DONORS_LIST_NODE = "DonorsList"
        const val STATUS = "status"
        const val ERROR = "error"
        const val LOGGED_EMAIL_ID = "emailId"

        fun hideKeyBoard(context: Activity) {
            try {
                val imm: InputMethodManager? = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                imm?.hideSoftInputFromWindow(context.currentFocus?.windowToken, 0)
            } catch (e: Exception) {
                // TODO: handle exception
                Log.e(AddBloodGroupInfoFragment.TAG, "hideKeyBoard: not able to hide keyboard",e)
            }
        }

        fun createDialog(context: Context, view: View) : AlertDialog {
            return AlertDialog.Builder(context)
                .setView(view)
                .setCancelable(false)
                .create()
        }

        fun createDialog(context: Context, message: String): AlertDialog {
            val progressView = LayoutInflater.from(context)
                .inflate(R.layout.progress_dialog,null,false);
            val loadingMessageTv:TextView = progressView.findViewById(R.id.supportText)
            loadingMessageTv.text = message
            return createDialog(context,progressView)
        }


        fun getSameBloodGroupPerson(type: String, personList: List<Person>): List<Person> {
            val sameBloodGroupPeopleList = mutableListOf<Person>()
            for(person in personList) {
                if (person.bloodGroup.equals(type)) {
                    sameBloodGroupPeopleList.add(person)
                }
            }
            return sameBloodGroupPeopleList
        }


    }
}