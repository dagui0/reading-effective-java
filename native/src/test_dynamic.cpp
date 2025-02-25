#include <iostream>
#include <slist.h>

using namespace std;

int main(void) {
    SimpleLinkedList *list = new SimpleLinkedList();

    list->add(1);
    list->add(2);
    list->add(3);
    list->add(4);
    list->print_dump();

    return 0;
}
