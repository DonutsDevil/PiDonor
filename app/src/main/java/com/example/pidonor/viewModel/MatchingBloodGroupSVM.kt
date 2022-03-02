package com.example.pidonor.viewModel

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.pidonor.Repository.FirebaseRepo
import com.example.pidonor.model.Person

class MatchingBloodGroupSVM : ViewModel() {

    private lateinit var emailId: String

    fun putValue(person: Person) : LiveData<Bundle> {
        return FirebaseRepo.addValue(person)
    }

    fun getALlBloodGroupPeople() : LiveData<List<Person>> {
        return FirebaseRepo.getAllBloodGroups()
    }

    fun setEmailId(email: String) {
        emailId = email
    }

    fun getEmailId():String {
        return emailId
    }
}