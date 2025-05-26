#include <stdio.h>

typedef int (*Operator)(int, int);

int add(int, int);

void main() {
    Operator plus = add;
    printf("%d + %d = %d\n", 3, 4, plus(3, 4));
}

int add(int a, int b) {
    return a + b;
}
