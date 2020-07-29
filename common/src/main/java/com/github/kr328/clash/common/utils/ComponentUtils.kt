package com.github.kr328.clash.common.utils

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import com.github.kr328.clash.common.Global
import kotlin.reflect.KClass

val KClass<*>.componentName: ComponentName
    get() = createRelativeComponentName(Global.application, this.java)

val KClass<*>.intent: Intent
    get() = Intent(Global.application, this.java)


fun <T> createRelativeComponentName(pkg: Context, cls: Class<T>): ComponentName {
    return createRelativeComponentName(pkg.packageName, cls.name)
}

fun createRelativeComponentName(pkg: String, cls: String): ComponentName {
    return ComponentName(pkg, if (cls[0] == '.') pkg + cls else cls)
}