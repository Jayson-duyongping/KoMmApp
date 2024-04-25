#include <jni.h>
#include <string>

//方法签名格式为 extern "C" JNIEXPORT 返回值类型 JNICALL Java_包名_类名_方法名(JNIEnv*,jobject,参数列表)
extern "C" JNIEXPORT jstring JNICALL
Java_com_jayson_komm_api_view_NDKActivity_stringFromJNI(JNIEnv *env,jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}