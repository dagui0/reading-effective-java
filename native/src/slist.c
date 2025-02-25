#include <stdio.h>
#include <stdlib.h>
#include <slist.h>

#if defined(__APPLE__) || defined(__OSX___)
#  ifdef SLIST_EXPORTS
#    define OSX_EXPORT __attribute__((visibility("default")))
#  else
#    define OSX_EXPORT
#  endif
#else
#  define OSX_EXPORT
#endif

#define TRUE    (1)
#define FALSE   (0)

OSX_EXPORT
SLIST *slist_create(void) {
    SLIST *list = (SLIST *)malloc(sizeof(SLIST));
    if (!list) {
        return NULL;
    }

    list->head = NULL;
    return list;
}

OSX_EXPORT
void slist_destroy(SLIST *list) {
    SLIST_NODE *temp = list->head;
    while (temp) {
        SLIST_NODE *next = temp->next;
        free(temp);
        temp = next;
    }
    free(list);
}

OSX_EXPORT
char *slist_msg(SLIST_CODE code) {
    switch (code) {
        case SLIST_NO_ERROR:
            return "OK";
        case SLIST_NOT_FOUND:
            return "Not found";
        case SLIST_INDEX_OUT_OF_RANGE:
            return "Index out of range";
        case SLIST_LIST_EMPTY:
            return "List empty";
        case SLIST_MEMORY_ALLOCATION_FAILED:
            return "Memory allocation failed";
        default:
            return "Unknown error";
    }
}


static SLIST_NODE *__find_last_node(SLIST *list) {
    SLIST_NODE *last = list->head;
    if (last == NULL)
        return NULL;

    while (last->next) {
        last = last->next;
    }
    return last;
}

static SLIST_NODE *__create_node(int data, SLIST_NODE *next) {
    SLIST_NODE *node = (SLIST_NODE *)malloc(sizeof(SLIST_NODE));
    if (!node) {
        return NULL;
    }

    node->data = data;
    node->next = next;
    return node;
}

OSX_EXPORT
SLIST_CODE slist_add(SLIST *list, int data) {
    SLIST_NODE *last = __find_last_node(list);

    SLIST_NODE *node = __create_node(data, NULL);
    if (!node)
        return SLIST_MEMORY_ALLOCATION_FAILED;

    if (last == NULL) {
        list->head = node;
        return SLIST_NO_ERROR;
    }
    else {
        last->next = node;
        return SLIST_NO_ERROR;
    }
}

OSX_EXPORT
SLIST_CODE slist_add_all(SLIST *list, const int *data, int length) {
    SLIST_NODE *last = __find_last_node(list);

    for (int i = 0; i < length; i++) {
        SLIST_NODE *node = __create_node(data[i], NULL);
        if (!node)
            return SLIST_MEMORY_ALLOCATION_FAILED;

        if (last == NULL)
            list->head = node;
        else
            last->next = node;
        last = node;
    }
    return SLIST_NO_ERROR;
}

OSX_EXPORT
SLIST_CODE slist_insert_at(SLIST *list, int index, int data) {
    if (index < 0)
        return SLIST_INDEX_OUT_OF_RANGE;

    if (index == 0) {
        SLIST_NODE *node = __create_node(data, list->head);
        if (!node)
            return SLIST_MEMORY_ALLOCATION_FAILED;
        list->head = node;
        return SLIST_NO_ERROR;
    }
    else {
        SLIST_NODE *temp = list->head;
        for (int i = 0; i < index - 1; i++) {
            if (!temp)
                return SLIST_INDEX_OUT_OF_RANGE;
            temp = temp->next;
        }
        if (!temp)
            return SLIST_INDEX_OUT_OF_RANGE;

        SLIST_NODE *node = __create_node(data, temp->next);
        if (!node)
            return SLIST_MEMORY_ALLOCATION_FAILED;
        temp->next = node;
        return SLIST_NO_ERROR;
    }
}

OSX_EXPORT
SLIST_CODE slist_remove_at(SLIST *list, int index) {
    if (index < 0)
        return SLIST_INDEX_OUT_OF_RANGE;

    if (index == 0) {
        SLIST_NODE *temp = list->head;
        list->head = list->head->next;
        free(temp);
    }
    else {
        SLIST_NODE *temp = list->head;
        for (int i = 0; i < index - 1; i++) {
            if (!temp)
                return SLIST_INDEX_OUT_OF_RANGE;
            temp = temp->next;
        }
        if (!temp || !temp->next)
            return SLIST_INDEX_OUT_OF_RANGE;

        SLIST_NODE *nodeToDelete = temp->next;
        temp->next = nodeToDelete->next;
        free(nodeToDelete);
    }
    return SLIST_NO_ERROR;
}

OSX_EXPORT
SLIST_CODE slist_remove_all(SLIST *list) {
    SLIST_NODE *temp = list->head;
    while (temp) {
        SLIST_NODE *next = temp->next;
        free(temp);
        temp = next;
    }
    list->head = NULL;
    return SLIST_NO_ERROR;
}

OSX_EXPORT
int slist_size(SLIST *list) {
    int size = 0;
    SLIST_NODE *last = list->head;
    while (last) {
        size++;
        last = last->next;
    }
    return size;
}

OSX_EXPORT
SLIST_CODE slist_get(SLIST *list, int index, int *data) {
    if (index < 0) {
        *data = 0;
        return SLIST_INDEX_OUT_OF_RANGE;
    }

    SLIST_NODE *temp = list->head;
    if (!temp) {
        *data = 0;
        return SLIST_LIST_EMPTY;
    }
    for (int i = 0; i < index; i++) {
        if (!temp) {
            *data = 0;
            return SLIST_INDEX_OUT_OF_RANGE;
        }
        temp = temp->next;
    }
    if (!temp) {
        *data = 0;
        return SLIST_INDEX_OUT_OF_RANGE;
    }

    *data = temp->data;
    return SLIST_NO_ERROR;
}

OSX_EXPORT
int slist_contains(SLIST *list, int data) {
    SLIST_NODE *temp = list->head;
    while (temp) {
        if (temp->data == data)
            return TRUE;
        temp = temp->next;
    }
    return FALSE;
}

OSX_EXPORT
void slist_print_dump(SLIST *list) {
    SLIST_NODE *temp = list->head;
    int size = slist_size(list);
    printf("SLIST(%d): ", size);
    while (temp) {
        printf("%d -> ", temp->data);
        temp = temp->next;
    }
    printf("NULL\n");
}
