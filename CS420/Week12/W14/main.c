#include <stdio.h>
#include <stdlib.h>

// Linked list
typedef struct Node {
    char *data;
    int isPalindrome;
    struct Node *next;
} Node;

int main(int argc, char **argv) {
    FILE *file = fopen(argv[1], "r");

    if (file == NULL) {
        printf("File not found\n");
        return 1;
    }

    Node *head = NULL;
    Node *current = NULL;

    char buffer[256];

    while (fscanf(file, "%s", buffer) != EOF) {
        Node *newNode = (Node *)malloc(sizeof(Node));
        newNode->data = (char *)malloc(strlen(buffer) + 1);
        strcpy(newNode->data, buffer);
        newNode->isPalindrome = 0;
        newNode->next = NULL;

        if (head == NULL) {
            head = newNode;
            current = head;
        } else {
            current->next = newNode;
            current = newNode;
        }
    }

    fclose(file);

    current = head;

    while (current != NULL) {
        char *data = current->data;
        int length = strlen(data);
        int isPalindrome = 1;

        for (int i = 0; i < length / 2; i++) {
            if (data[i] != data[length - i - 1]) {
                isPalindrome = 0;
                break;
            }
        }

        current->isPalindrome = isPalindrome;
        current = current->next;
    }

    // Remove non-palindromes
    Node *prev = NULL;
    current = head;

    while (current != NULL) {
        if (current->isPalindrome == 0) {
            if (prev == NULL) {
                head = current->next;
                free(current->data);
                free(current);
                current = head;
            } else {
                prev->next = current->next;
                free(current->data);
                free(current);
                current = prev->next;
            }
        } else {
            prev = current;
            current = current->next;
        }
    }

    current = head;

    while (current != NULL) {
        printf("%s\n", current->data);
        current = current->next;
    }
}