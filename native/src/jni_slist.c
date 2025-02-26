#include <slist.h>
#include <com_yidigun_utils_JniSlist.h>

#ifdef __cplusplus
extern "C" {
#endif

static jclass find_exception_class_by_code(JNIEnv *env, SLIST_CODE code) {
    switch (code) {
        case SLIST_NO_ERROR:
            return NULL;
        case SLIST_INDEX_OUT_OF_RANGE:
            return (*env)->FindClass(env, "java/lang/IndexOutOfBoundsException");
        case SLIST_LIST_EMPTY:
            return (*env)->FindClass(env, "java/lang/IllegalArgumentException");
        case SLIST_MEMORY_ALLOCATION_FAILED:
            return (*env)->FindClass(env, "java/lang/OutOfMemoryError");
        default:
            return NULL;
    }
}

jlong JNICALL Java_com_yidigun_utils_JniSlist_create(JNIEnv *env, jobject obj) {
    return (jlong) slist_create();
}

void JNICALL Java_com_yidigun_utils_JniSlist_destroy(JNIEnv *env, jobject obj, jlong pointer) {
    slist_destroy((SLIST *)pointer);
}

void JNICALL Java_com_yidigun_utils_JniSlist_add(JNIEnv *env, jobject obj, jlong pointer, jint value) {
    int rs = slist_add((SLIST *)pointer, (int)value);
    if (rs != SLIST_NO_ERROR) {
        jclass ex_class = find_exception_class_by_code(env, rs);
        if (ex_class) {
            if ((*env)->ExceptionOccurred(env))
                (*env)->ExceptionClear(env);
            (*env)->ThrowNew(env, ex_class, slist_msg(rs));
        }
    }
}

void JNICALL Java_com_yidigun_utils_JniSlist_addAll(JNIEnv *env, jobject obj, jlong pointer, jintArray values) {
    jsize size = (*env)->GetArrayLength(env, values);
    jint *elements = (*env)->GetIntArrayElements(env, values, 0);
    SLIST_CODE rs = slist_add_all((SLIST *)pointer, (int *)elements, (int)size);
    (*env)->ReleaseIntArrayElements(env, values, elements, 0);
    if (rs != SLIST_NO_ERROR) {
        jclass ex_class = find_exception_class_by_code(env, rs);
        if (ex_class) {
            if ((*env)->ExceptionOccurred(env))
                (*env)->ExceptionClear(env);
            (*env)->ThrowNew(env, ex_class, slist_msg(rs));
        }
    }
}

void JNICALL Java_com_yidigun_utils_JniSlist_insertAt(JNIEnv *env, jobject obj, jlong pointer, jint index, jint value) {
    SLIST_CODE rs = slist_insert_at((SLIST *)pointer, (int)index, (int)value);
    if (rs != SLIST_NO_ERROR) {
        jclass ex_class = find_exception_class_by_code(env, rs);
        if (ex_class) {
            if ((*env)->ExceptionOccurred(env))
                (*env)->ExceptionClear(env);
            (*env)->ThrowNew(env, ex_class, slist_msg(rs));
        }
    }
}

void JNICALL Java_com_yidigun_utils_JniSlist_removeAt(JNIEnv *env, jobject obj, jlong pointer, jint index) {
    SLIST_CODE rs = slist_remove_at((SLIST *)pointer, (int)index);
    if (rs != SLIST_NO_ERROR) {
        jclass ex_class = find_exception_class_by_code(env, rs);
        if (ex_class) {
            if ((*env)->ExceptionOccurred(env))
                (*env)->ExceptionClear(env);
            (*env)->ThrowNew(env, ex_class, slist_msg(rs));
        }
    }
}

void JNICALL Java_com_yidigun_utils_JniSlist_removeAll(JNIEnv *env, jobject obj, jlong pointer) {
    SLIST_CODE rs = slist_remove_all((SLIST *)pointer);
    if (rs != SLIST_NO_ERROR) {
        jclass ex_class = find_exception_class_by_code(env, rs);
        if (ex_class) {
            if ((*env)->ExceptionOccurred(env))
                (*env)->ExceptionClear(env);
            (*env)->ThrowNew(env, ex_class, slist_msg(rs));
        }
    }
}

jint JNICALL Java_com_yidigun_utils_JniSlist_size(JNIEnv *env, jobject obj, jlong pointer) {
    return (jint) slist_size((SLIST *)pointer);
}

jint JNICALL Java_com_yidigun_utils_JniSlist_get(JNIEnv *env, jobject obj, jlong pointer, jint index) {
    int value = 0;
    SLIST_CODE rs = slist_get((SLIST *)pointer, (int)index, &value);
    if (rs != SLIST_NO_ERROR) {
        jclass ex_class = find_exception_class_by_code(env, rs);
        if (ex_class) {
            if ((*env)->ExceptionOccurred(env))
                (*env)->ExceptionClear(env);
            (*env)->ThrowNew(env, ex_class, slist_msg(rs));
        }
        return 0;
    }
    else {
        return (jint)value;
    }
}

jboolean JNICALL Java_com_yidigun_utils_JniSlist_contains(JNIEnv * env, jobject obj, jlong pointer, jint value) {
    return (jboolean) slist_contains((SLIST *)pointer, (int)value);
}

#ifdef __cplusplus
}
#endif
