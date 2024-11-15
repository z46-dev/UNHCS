#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#define MAX_SIZE 1042
#define BUFFER_SIZE 128

/**
 * Linked List
 * 
 * Word is an unspecified size character array
 * isPalindrome is a bit field that stores whether the word is a palindrome or not
 *     - 0: Not a palindrome
 *     - 1: Palindrome
 *     - Only takes up 1 bit of memory
 * next is a pointer to the next node in the linked list
 * 
 * LinkedList is a linked list that stores a head and tail node
 * 
 * insert adds a node to the linked list
 * removeNode removes a node from the linked list
 * print prints the linked list
 */

typedef struct Node {
    char *word;
    unsigned int isPalindrome : 1;
    struct Node *next;
} Node;

typedef struct LinkedList {
    Node *head;
    Node *tail;
} LinkedList;

/**
 * Adds a node to the linked list
 */
void insert(LinkedList *list, Node *node) {
    if (list->head == NULL) {
        list->head = node;
        list->tail = node;
    } else {
        list->tail->next = node;
        list->tail = node;
    }
}

/**
 * Removes a node from the linked list
 * Will free the memory allocated for the node
 */
void removeNode(LinkedList *list, Node *node) {
    if (list->head == node) {
        list->head = node->next;
    } else {
        Node *current = list->head;

        while (current != NULL) {
            if (current->next == node) {
                current->next = node->next;
                break;
            }

            current = current->next;
        }
    }

    free(node);
}

/**
 * Prints the linked list
 */
void print(LinkedList *list) {
    Node *current = list->head;

    while (current != NULL) {
        printf("%s (%d)", current->word, current->isPalindrome);
        current = current->next;

        if (current != NULL) {
            printf(" -> ");
        }
    }

    printf("\n");
}

/**
 * Reads a file and stores each word in the file in the words array
 * Strings are dynamically allocated
 */
int readFile(char *fileName, char **words) {
    FILE *file = fopen(fileName, "r");

    if (file == NULL) {
        return -1;
    }

    char buffer[BUFFER_SIZE];
    int i = 0;

    // Scan until new line
    while (fgets(buffer, sizeof(buffer), file) != NULL) {
        buffer[strcspn(buffer, "\n")] = '\0';
        words[i] = (char *)malloc(strlen(buffer) + 1);
        strcpy(words[i], buffer);
        i++;
    }

    fclose(file);
    return i;
}

/**
 * Checks if a word is a palindrome
 */
int isPalindrome(char *word) {
    int length = strlen(word);
    
    for (int i = 0; i < length; i ++) {
        if (word[i] != word[length - i - 1]) {
            return 0;
        }
    }
    
    return 1;
}

char* turnComplexPalindromeIntoSimplePalindrome(char *input) {
    // Just return a string without any non alphabetic chars
    char *output = (char *)malloc(strlen(input) + 1);
    
    int j = 0;
    for (int i = 0; i < strlen(input); i++) {
        if (isalpha(input[i])) {
            output[j] = input[i];
            j++;
        }
    }

    output[j] = '\0';

    return output;
}

int main(int argc, char **argv) {
    if (argc != 2) {
        printf("Usage: %s <file>\n", argv[0]);
        return 1;
    }

    char *words[MAX_SIZE];
    int size = readFile(argv[1], words);

    if (size == -1) {
        printf("Error reading file\n");
        return 1;
    }

    LinkedList list = {NULL, NULL};

    for (int i = 0; i < size; i++) {
        Node *node = (Node *)malloc(sizeof(Node));
        node->word = words[i];
        node->isPalindrome = isPalindrome(turnComplexPalindromeIntoSimplePalindrome(strlwr(words[i])));
        node->next = NULL;
        insert(&list, node);
    }

    print(&list);
    printf("\n");

    Node *current = list.head;
    while (current != NULL) {
        if (!current->isPalindrome) {
            Node *next = current->next;
            removeNode(&list, current);
            current = next;
        } else {
            current = current->next;
        }
    }

    print(&list);

    return 0;
}