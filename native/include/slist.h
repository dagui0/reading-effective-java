#ifndef _SIMPLELINKEDLIST_H_
#define _SIMPLELINKEDLIST_H_

#ifdef WINDOWS
#  ifdef SLIST_STATIC
#    define SLISTAPI
#  else
#    ifdef SLIST_EXPORTS
#      define SLISTAPI __declspec(dllexport)
#    else
#      define SLISTAPI __declspec(dllimport)
#    endif
#  endif
#else
#  define SLISTAPI
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
    virtual ~SimpleLinkedList();

    virtual void add(const int value);
    virtual void addAll(const int *values, const int length);
    virtual void insertAt(const int index, const int value);
    virtual void removeAt(const int index);
    virtual void removeAll();

    virtual int size();
    virtual int get(const int index);
    virtual bool contains(const int value);

    virtual std::string dump();
    virtual void print_dump();

    enum ErrorCode {
        NO_ERROR,
        INDEX_OUT_OF_RANGE,
        MEMORY_ALLOCATION_FAILED
    };

    class SLISTAPI Error {
    public:
        Error(ErrorCode code, int index, int data);
	//virtual ~Error();
        virtual ErrorCode getCode();
        virtual int getIndex();
        virtual int getData();
    private:
        ErrorCode code;
        int index;
        int data;
    };
};

#ifdef __APPLE__ 

extern "C" {

    SimpleLinkedList *NewSimpleLinkedList(void);
    typedef SimpleLinkedList *SimpleLinkedList_creator(void);

    void DeleteSimpleLinkedList(SimpleLinkedList*);
    typedef void SimpleLinkedList_disposer(SimpleLinkedList*);

/*
    SimpleLinkedList::Error *NewSimpleLinkedListError(SimpleLinkedList::ErrorCode,int,int);
    typedef SimpleLinkedList::Error *SimpleLinkedListError_creator(SimpleLinkedList::ErrorCode,int,int);

    void DeleteSimpleLinkedListError(SimpleLinkedList::Error*);
    typedef void SimpleLinkedListError_disposer(SimpleLinkedList::Error*);
*/
}

#endif

#endif
