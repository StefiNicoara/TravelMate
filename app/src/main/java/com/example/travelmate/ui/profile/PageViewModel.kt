package com.example.travelmate.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PageViewModel : ViewModel() {

    private val _index = MutableLiveData<Int>()
    val text: LiveData<String> = Transformations.map(_index) {
        "Hello world from section: $it" + "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent varius nisl massa, pellentesque varius ipsum commodo eget. Phasellus hendrerit consectetur libero non vehicula. Vestibulum iaculis augue ex, at fermentum nunc mollis eu. Aenean consectetur massa eget sagittis feugiat. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Phasellus nec pulvinar ligula. Aliquam lacinia ex turpis, ac ultricies odio sagittis sit amet. Etiam interdum, ligula vel elementum facilisis, nisi libero rhoncus neque, ac auctor lacus nibh hendrerit ipsum. Nulla sed cursus orci. Nullam eu elementum purus. Nullam sed lorem a augue hendrerit efficitur. Maecenas pretium urna vel tellus lobortis malesuada. Duis porta ipsum a diam ornare scelerisque.\n" +
                "\n" +
                "Cras vehicula, arcu lobortis dignissim rhoncus, diam urna ultricies mi, ac feugiat mi ipsum ac dolor. Praesent scelerisque quam eget augue convallis, id dignissim erat convallis. Vivamus quis efficitur ipsum. Cras interdum congue ullamcorper. Etiam placerat lobortis purus in lobortis. Quisque quis egestas metus. Integer varius condimentum eros vel sodales.\n" +
                "\n" +
                "Cras a velit vulputate, tristique leo id, pulvinar neque. Duis placerat lectus at semper feugiat. Pellentesque varius, ante a cursus sagittis, nibh justo viverra urna, vel ullamcorper ante metus at elit. Suspendisse at tortor ullamcorper, ornare dolor vitae, iaculis libero. Phasellus sit amet ex sit amet massa iaculis feugiat. Praesent vehicula eros nec lacus faucibus, sit amet tincidunt nisl lacinia. Aliquam lobortis mauris at urna ultrices laoreet. Sed hendrerit est dui. In orci eros, commodo sed erat sit amet, tempus rutrum elit. Nulla pulvinar scelerisque auctor. Nam eleifend finibus risus, fermentum porta purus faucibus non. Mauris id placerat massa. Vestibulum scelerisque sagittis diam euismod lobortis. Duis viverra lacus nec est sagittis, in molestie risus porttitor."
    }

    fun setIndex(index: Int) {
        _index.value = index
    }
}