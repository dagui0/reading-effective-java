#include <stdio.h>

int *complex_arg(int **a, int *b[]) {
    return NULL;
}

int *complex_sig(int *(*farg)(int**, int**)) {
    return farg(NULL, NULL);
}

int main(void) {
    int *(*fsig)(int *(*)(int**,int**)) = complex_sig;

    int (*fptr)(const char *restrict, ...) = printf;
    fptr("Hello, %s!\n", "world");
    return 0;
}

