#ifndef _SIMPLELINKEDLIST_H_
#define _SIMPLELINKEDLIST_H_

#ifdef SLIST_STATIC
#  define SLISTAPI
#else
#  ifdef SLIST_EXPORTS
#    define SLISTAPI __declspec(dllexport)
#  else
#    define SLISTAPI __declspec(dllimport)
#  endif
#endif

class SLISTAPI SimpleLinkedList {
private:
    class Node {
    public:
        Node(int data);
        int getData();
        Node *getNext();
        void setNext(Node *next);
    private:
        int data;
        Node* next;
    };

    Node *head;

public:
    SimpleLinkedList();
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

    class SLISTAPI Error {
    public:
        Error(ErrorCode code, int index, int data);
        ErrorCode getCode();
        int getIndex();
        int getData();
    private:
        ErrorCode code;
        int index;
        int data;
    };
};

#endif
