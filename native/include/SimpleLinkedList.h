#ifndef _SIMPLELINKEDLIST_H_
#define _SIMPLELINKEDLIST_H_

class SimpleLinkedList {
private:
    class Node {
    public:
        Node(int data): data(data), next(NULL) {}
        int getData() { return this->data; }
        Node *getNext() { return this->next; }
        void setNext(Node *next) { this->next = next; }
    private:
        int data;
        Node* next;
    };

    Node *head;

public:
    SimpleLinkedList() : head(NULL) {}
    ~SimpleLinkedList();

    void add(const int value);
    void addAll(const int *values, const int length);
    void insertAt(const int index, const int value);
    void removeAt(const int index);
    void removeAll();

    int size();
    int get(const int index);
    bool contains(const int value);

    std::string dump();
    void print_dump();

    enum ErrorCode {
        NO_ERROR,
        INDEX_OUT_OF_RANGE,
        MEMORY_ALLOCATION_FAILED
    };

    class Error {
    public:
        Error(ErrorCode code, int index, int data) : code(code), index(index), data(data) {}
        ErrorCode getCode() { return code; }
        int getIndex() { return index; }
        int getData() { return data; }
    private:
        ErrorCode code;
        int index;
        int data;
    };
};

#endif
