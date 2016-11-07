//
// Created by Lulu on 2016/11/7.
//

#ifndef TUYOU_NATIVE_KEY_H
#define TUYOU_NATIVE_KEY_H
#include <jni.h>
JNIEXPORT jstring JNICALL
Java_com_myxfd_tuyou_ndk_NativeHelper_getPasswrodKey(JNIEnv *env, jclass type);
#endif //TUYOU_NATIVE_KEY_H
