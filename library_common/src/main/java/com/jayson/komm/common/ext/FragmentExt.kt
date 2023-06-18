package com.jayson.komm.common.ext

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

fun FragmentActivity.replaceFragment(containerViewId: Int, fragment: Fragment) {
    supportFragmentManager.beginTransaction()
        .replace(containerViewId, fragment)
        .commit()
}

fun FragmentActivity.replaceFragment(containerViewId: Int, fragment: Fragment, tag: String) {
    supportFragmentManager.beginTransaction()
        .replace(containerViewId, fragment, tag)
        .commit()
}

fun FragmentActivity.addFragment(containerViewId: Int, fragment: Fragment) {
    supportFragmentManager.beginTransaction()
        .add(containerViewId, fragment)
        .commit()
}

fun FragmentActivity.addFragment(containerViewId: Int, fragment: Fragment, tag: String) {
    supportFragmentManager.beginTransaction()
        .add(containerViewId, fragment, tag)
        .commit()
}

fun FragmentActivity.showFragment(fragment: Fragment) {
    supportFragmentManager.beginTransaction()
        .show(fragment)
        .commit()
}

fun FragmentActivity.hideFragment(fragment: Fragment) {
    supportFragmentManager.beginTransaction()
        .hide(fragment)
        .commit()
}