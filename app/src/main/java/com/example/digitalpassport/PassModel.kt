package com.example.digitalpassport

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PassViewModel : ViewModel(){
    private var listOfPasses = MutableLiveData<List<Pair<String, Pass>>>()

    private val selectedPass = MutableLiveData<Pass>()

    private var user = MutableLiveData<User>()

    private var time = MutableLiveData<Long>()

    private var ready = MutableLiveData<Long>()


    fun setPassesList(passes: List<Pair<String, Pass>>) {
        listOfPasses.value = passes
    }

    fun getPassesList(): LiveData<List<Pair<String, Pass>>> {
        return listOfPasses
    }

    fun setSelectedPass(pass: Pass) {
        selectedPass.value = pass
    }

    fun getSelectedPass(): LiveData<Pass> {
        return selectedPass
    }


    fun setUser(pass: User) {
        user.value = pass
    }

    fun getUser(): LiveData<User> {
        return user
    }
    fun setTime(timeCred: Long) {
        time.value = timeCred
    }

    fun getTime(): LiveData<Long> {
        return time
    }

    fun setReady(readyCred: Long) {
        ready.value = readyCred
    }

    fun getReady(): LiveData<Long> {
        return ready
    }


}
