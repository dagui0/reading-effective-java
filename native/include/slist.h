#ifndef __SLIST_H
#define __SLIST_H
#ifdef __cplusplus
extern "C" {
#endif

#if defined(WINDOWS) || defined(_WIN32)
#  ifdef SLIST_STATIC
#    define SLIST_API
#  else
#    ifdef SLIST_EXPORTS
#      define SLIST_API __declspec(dllexport)
#    else
#      define SLIST_API __declspec(dllimport)
#    endif
#  endif
#else
#  define SLIST_API
#endif

struct __slist_node {
    int data;
    struct __slist_node *next;
};
typedef struct __slist_node SLIST_NODE;

struct __slist {
    struct __slist_node *head;
};
typedef struct __slist SLIST;

enum __slist_code {
    SLIST_NO_ERROR,
    SLIST_NOT_FOUND,
    SLIST_INDEX_OUT_OF_RANGE,
    SLIST_LIST_EMPTY,
    SLIST_MEMORY_ALLOCATION_FAILED
};
typedef enum __slist_code SLIST_CODE;


SLIST_API
SLIST *slist_create(void);

SLIST_API
void slist_destroy(SLIST *list);

SLIST_API
char *slist_msg(SLIST_CODE code);

SLIST_API
SLIST_CODE slist_add(SLIST *list, int data);

SLIST_API
SLIST_CODE slist_add_all(SLIST *list, const int *data, int length);

SLIST_API
SLIST_CODE slist_insert_at(SLIST *list, int index, int data);

SLIST_API
SLIST_CODE slist_remove_at(SLIST *list, int index);

SLIST_API
SLIST_CODE slist_remove_all(SLIST *list);

SLIST_API
int slist_size(SLIST *list);

SLIST_API
SLIST_CODE slist_get(SLIST *list, int index, int *data);

SLIST_API
int slist_contains(SLIST *list, int data);


SLIST_API
void slist_print_dump(SLIST *list);

#ifdef __cplusplus
}
#endif
#endif  /* !__SLIST_H */
