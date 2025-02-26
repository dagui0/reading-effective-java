#include <stdio.h>
#include <slist.h>

int main(void) {

    SLIST *list = slist_create();
    if (!list) {
        printf("failed to create list.\n");
        return 1;
    }

    int tc = 0;
    int value = 0, size = 0;
    SLIST_CODE rs = SLIST_NO_ERROR;

    slist_print_dump(list);

    // 1. get first when empty
    rs = slist_get(list, 0, &value);
    printf("%02d: get(%d) = %d: %d (%s)\n", (++tc), 0, value, rs, slist_msg(rs));

    // 2~6. add
    for (int i = 0; i < 5; i++) {
        rs = slist_add(list, i + 1);
        printf("%02d: add(%d): %d (%s)\n", (++tc), i + 1, rs, slist_msg(rs));
        slist_print_dump(list);
    }

    // 7. add_all
    int arr1[] = { 6, 7, 8, 9, 10 };
    rs = slist_add_all(list, arr1, 5);
    printf("%02d: add_all(6, 7, 8, 9, 10): %d (%s)\n", (++tc), rs, slist_msg(rs));
    slist_print_dump(list);

    // 8~14. get
    size = slist_size(list);
    int arr2[] = { -1, 0, 3, size - 1, size, size + 1, 10000 };
    for (int i = 0; i < 7; i++) {
        rs = slist_get(list, arr2[i], &value);
        printf("%02d: get(%d) = %d: %d (%s)\n", (++tc), arr2[i], value, rs, slist_msg(rs));
    }

    // 15. insert at minus index
    value = -1;
    rs = slist_insert_at(list, value, value * 100);
    printf("%02d: insert_at(%d, %d): %d (%s)\n", (++tc), value, value * 100, rs, slist_msg(rs));
    slist_print_dump(list);

    // 16. insert at head
    value = 0;
    rs = slist_insert_at(list, value, value * 100);
    printf("%02d: insert_at(%d, %d): %d (%s)\n", (++tc), value, value * 100, rs, slist_msg(rs));
    slist_print_dump(list);

    // 17. insert at 3
    value = 3;
    rs = slist_insert_at(list, value, value * 100);
    printf("%02d: insert_at(%d, %d): %d (%s)\n", (++tc), value, value * 100, rs, slist_msg(rs));
    slist_print_dump(list);

    // 18. insert at last
    value = slist_size(list) - 1;
    rs = slist_insert_at(list, value, value * 100);
    printf("%02d: insert_at(%d, %d): %d (%s)\n", (++tc), value, value * 100, rs, slist_msg(rs));
    slist_print_dump(list);

    // 19. insert after last
    value = slist_size(list);
    rs = slist_insert_at(list, value, value * 100);
    printf("%02d: insert_at(%d, %d): %d (%s)\n", (++tc), value, value * 100, rs, slist_msg(rs));
    slist_print_dump(list);

    // 20. insert at last + 2
    value = slist_size(list) + 1;
    rs = slist_insert_at(list, value, value * 100);
    printf("%02d: insert_at(%d, %d): %d (%s)\n", (++tc), value, value * 100, rs, slist_msg(rs));
    slist_print_dump(list);

    // 21. remove minus index
    value = -1;
    rs = slist_remove_at(list, value);
    printf("%02d: remove_at(%d): %d (%s)\n", (++tc), value, rs, slist_msg(rs));
    slist_print_dump(list);

    // 22. remove head
    value = 0;
    rs = slist_remove_at(list, value);
    printf("%02d: remove_at(%d): %d (%s)\n", (++tc), value, rs, slist_msg(rs));
    slist_print_dump(list);

    // 23. remove at 3
    value = 3;
    rs = slist_remove_at(list, value);
    printf("%02d: remove_at(%d): %d (%s)\n", (++tc), value, rs, slist_msg(rs));
    slist_print_dump(list);

    // 24. remove last
    value = slist_size(list) - 1;
    rs = slist_remove_at(list, value);
    printf("%02d: remove_at(%d): %d (%s)\n", (++tc), value, rs, slist_msg(rs));
    slist_print_dump(list);

    // 25. remove after last
    value = slist_size(list);
    rs = slist_remove_at(list, value);
    printf("%02d: remove_at(%d): %d (%s)\n", (++tc), value, rs, slist_msg(rs));
    slist_print_dump(list);

    // 26. remove at last + 1
    value = slist_size(list) + 1;
    rs = slist_remove_at(list, value);
    printf("%02d: remove_at(%d): %d (%s)\n", (++tc), value, rs, slist_msg(rs));
    slist_print_dump(list);

    // 27. remove all
    rs = slist_remove_all(list);
    printf("%02d: remove_all(): %d (%s)\n", (++tc), rs, slist_msg(rs));
    slist_print_dump(list);

    // 28. add all
    int final_array[] = { 1, 4, 7, 8, 9, 6, 3, 2, 5 };
    rs = slist_add_all(list, final_array, 9);
    printf("%02d: add_all(1, 4, 7, 8, 9, 6, 3, 2, 5): %d (%s)\n", (++tc), rs, slist_msg(rs));
    slist_print_dump(list);

    // 29~33. contains
    int arr3[] = { 8, 3, 21, -1, 2 };
    for (int i = 0; i < 5; i++) {
        rs = slist_contains(list, arr3[i]);
        printf("%02d: contains(%d): %d (%s)\n", (++tc), arr3[i], rs, (rs)? "TRUE": "FALSE");
    }

    // 34. destroy
    printf("%02d: destroy()\n", (++tc));
    slist_destroy(list);

    return 0;
}
