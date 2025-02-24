#include <iostream>
#include <SimpleLinkedList.h>

using namespace std;

int main(void) {
    SimpleLinkedList *list = new SimpleLinkedList();

    list->print_dump();
    cout << "adding " << 1 << endl;
    list->add(1);
    list->print_dump();
    cout << "adding " << 2 << endl;
    list->add(2);
    list->print_dump();
    cout << "adding " << 3 << endl;
    list->add(3);
    list->print_dump();
    cout << "adding " << 4 << endl;
    list->add(4);
    list->print_dump();
    cout << "adding " << 5 << endl;
    list->add(5);
    list->print_dump();

    cout << "adding { " << 6 << ", " << 7 << ", " << 8  << ", " << 9 << ", " << 10 << " }" << endl;
    int array[] { 6, 7, 8, 9, 10 };
    list->addAll(array, 5);
    list->print_dump();

    cout << "getting 3rd: " << list->get(2) << endl;
    cout << "getting 5th: " << list->get(4) << endl;

    try {
        cout << "try getting at " << -1 << endl;
        list->get(-1);
    }
    catch (SimpleLinkedList::Error *e) {
        cout << "exception caught: code=" << e->getCode() << ", index=" << e->getIndex() << ", data=" << e->getData() << endl;
        delete e;
    }

    cout << "inserting at " << 2 << ", " << 10 << endl;
    list->insertAt(2, 10);
    list->print_dump();
    cout << "removing at " << 2 << endl;
    list->removeAt(2);
    list->print_dump();

    try {
        cout << "try inserting at " << 1000 << endl;
        list->insertAt(1000, 0);
    }
    catch (SimpleLinkedList::Error *e) {
        cout << "exception caught: code=" << e->getCode() << ", index=" << e->getIndex() << ", data=" << e->getData() << endl;
        delete e;
    }

    try {
        cout << "try removing at " << -1 << endl;
        list->removeAt(-1);
    }
    catch (SimpleLinkedList::Error *e) {
        cout << "exception caught: code=" << e->getCode() << ", index=" << e->getIndex() << ", data=" << e->getData() << endl;
        delete e;
    }

    cout << "removing all" << endl;
    list->removeAll();
    list->print_dump();

    cout << "destructor test" << endl;
    int final_array[] { 1, 4, 7, 8, 9, 6, 3, 2, 5 };
    list->addAll(final_array, 9);
    list->print_dump();
    delete list;

    return 0;
}
