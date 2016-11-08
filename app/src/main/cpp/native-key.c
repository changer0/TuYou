//
// Created by Lulu on 2016/11/7.
//

#include "native-key.h"


JNIEXPORT jstring JNICALL
Java_com_myxfd_tuyou_ndk_NativeHelper_getPasswrodKey(JNIEnv *env, jclass type) {

    // TODO
    char *passwordKey = "womenshizuiniubideren";


    return (*env)->NewStringUTF(env, passwordKey);
}