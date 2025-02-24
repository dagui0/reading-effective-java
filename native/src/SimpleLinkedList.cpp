#include <iostream>
#include <SimpleLinkedList.h>

using namespace std;

SimpleLinkedList::~SimpleLinkedList() {
    Node *temp = this->head;
    while (temp) {
#ifdef DEBUG
        cout << "deleting " << temp->getData() << " -> ";
#endif
        Node *next = temp->getNext();
        delete temp;
        temp = next;
    }
#ifdef DEBUG
    cout << "all deleted.";
#endif
}

void SimpleLinkedList::add(const int value) {
    Node* newNode = new Node(value);
    if (!newNode)
        throw new Error(MEMORY_ALLOCATION_FAILED, -1, value);

    if (!this->head)
        head = newNode;
    else {
        Node *last = head;
        while (last->getNext())
            last = last->getNext();
        last->setNext(newNode);
    }
}

void SimpleLinkedList::addAll(const int *values, const int length) {
    for (int i = 0; i < length; i++)
        add(values[i]);
}

void SimpleLinkedList::insertAt(const int index, const int value) {
    if (index < 0)
        throw new Error(INDEX_OUT_OF_RANGE, index, value);

    Node *newNode = new Node(value);
    if (!newNode)
        throw new Error(MEMORY_ALLOCATION_FAILED, -1, value);

    if (index == 0) {
        newNode->setNext(this->head);
        this->head = newNode;
    }
    else {
        Node *atIndex = this->head;
        for (int i = 0; i < index - 1; i++) {
            if (!atIndex) {
                delete newNode;
                throw new Error(INDEX_OUT_OF_RANGE, index, value);
            }
            atIndex = atIndex->getNext();
        }
        if (!atIndex) {
            delete newNode;
            throw new Error(INDEX_OUT_OF_RANGE, index, value);
        }

        newNode->setNext(atIndex->getNext());
        atIndex->setNext(newNode);
    }
}

void SimpleLinkedList::removeAt(const int index) {
    if (index < 0)
        throw new Error(INDEX_OUT_OF_RANGE, index, -1);

    if (index == 0) {
        Node *temp = this->head;
        this->head = this->head->getNext();
        delete temp;
    }
    else {
        Node *temp = this->head;
        for (int i = 0; i < index - 1; i++) {
            if (!temp) {
                throw new Error(INDEX_OUT_OF_RANGE, index, -1);
            }
            temp = temp->getNext();
        }
        if (!temp) {
            throw new Error(INDEX_OUT_OF_RANGE, index, -1);
        }

        Node *nodeToDelete = temp->getNext();
        temp->setNext(nodeToDelete->getNext());
        delete nodeToDelete;
    }
}

void SimpleLinkedList::removeAll() {
    Node *temp = this->head;
    while (temp) {
        Node *next = temp->getNext();
        delete temp;
        temp = next;
    }
    head = NULL;
}

int SimpleLinkedList::size() {
    int size = 0;
    Node* last = this->head;
    while (last) {
        size++;
        last = last->getNext();
    }
    return size;
}

int SimpleLinkedList::get(const int index) {
    if (index < 0)
        throw new Error(INDEX_OUT_OF_RANGE, index, -1);

    Node *temp = this->head;
    for (int i = 0; i < index; i++) {
        if (!temp) {
            throw new Error(INDEX_OUT_OF_RANGE, index, -1);
        }
        temp = temp->getNext();
    }
    if (!temp) {
        throw new Error(INDEX_OUT_OF_RANGE, index, -1);
    }

    return temp->getData();
}

bool SimpleLinkedList::contains(const int value) {
    Node *temp = this->head;
    while (temp) {
        if (temp->getData() == value)
            return true;
        temp = temp->getNext();
    }
    return false;
}

string SimpleLinkedList::dump() {
    string s;
    s.reserve(8192);

    if (!this->head) {
        s.append("(empty)");
        return s;
    }

    Node* temp = this->head;
    while (temp) {
        s.append(to_string(temp->getData())).append(" -> ");
        temp = temp->getNext();
    }
    s.append("NULL");
    return s;
}

void SimpleLinkedList::print_dump() {
    cout << "SimpleLinkedList(" << size() << "): " << dump() << endl;
}
