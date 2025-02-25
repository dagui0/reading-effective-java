#include <stdio.h>
#include <slist.h>

int main(void) {
    SLIST *list = slist_create();

    slist_add(list, 1);
    slist_add(list, 2);
    slist_add(list, 3);
    slist_add(list, 4);

    int size = slist_size(list);
    for (int i = 0; i < size; i++) {
        int value = 0;
        SLIST_CODE rs = slist_get(list, i, &value);
        printf("list[%d] == %d: %d (%s)\n", i, value, rs, slist_msg(rs));
    }

    SLIST_CODE rs = slist_insert_at(list, 10000, 1000);
    printf("list[%d] = %d: %d (%s)\n", 10000, 1000, rs, slist_msg(rs));

    slist_destroy(list);
    return 0;
}
