#ifndef HUFFMAN_CPP
#define HUFFMAN_CPP

#include <bits/stdc++.h>
using namespace std;

// ******************PUBLIC OPERATIONS*********************
// void symbolCounts1(fstream &Operation, int hash_table[]) --> Count the frequency of each symbol in the file
// void HuffmanTreeLoad(HuffmanNode *HuffmanTree[], int hash_table[]) --> Load the Huffman tree from the frequency table
// void HuffmanTreeLoad2(HuffmanNode *HuffmanTree[], int Huffmansize) --> Load the Huffman tree from the frequency table
// void HuffmanTreeBuild(HuffmanNode *HuffmanTree[], int Huffmansize) --> Build the Huffman tree
// void HuffmanTransfer(HuffmanNode *n, string s) --> Transfer the Huffman tree to a code table
// void writedat() --> Write the encoded data to the output file
// void encode() --> Perform the entire encoding process
// ******************ERRORS********************************
// Prints error messages for file operations

string file_name;
fstream Operation;
fstream Huffdat;
fstream HuffTree;

int hash_table[256];

// Count the frequency of each symbol in the file
// Operation: input file stream
// hash_table: frequency table
void symbolCounts1(fstream &Operation, int hash_table[])
{
    if (!Operation)
    {
        cerr << "File Operation could not be open!" << endl;
        exit(1);
    }
    Operation.seekg(0, ios::beg);

    char a;
    while (Operation.read(reinterpret_cast<char *>(&a), sizeof(a)))
    {
        int n = static_cast<int>(a) + 128;
        hash_table[n]++;
    }
    Operation.close();
    cout << "symbolCounts successfully\n";
}

// Class representing a node in the Huffman tree
class HuffmanNode
{
public:
    char value;
    int frequency;
    bool flag = true;
    HuffmanNode *left;
    HuffmanNode *right;

    HuffmanNode(char _value = '\0', int _frequency = 0)
    {
        value = _value;
        frequency = _frequency;
        left = nullptr;
        right = nullptr;
    }
};

// Global variables for Huffman tree and size
HuffmanNode *HuffmanTree[256];
int Huffmansize = 0;

// Load the Huffman tree from the frequency table
// HuffmanTree: array of Huffman tree nodes
// hash_table: frequency table
void HuffmanTreeLoad(HuffmanNode *HuffmanTree[], int hash_table[])
{
    for (int i = 0; i < 256; i++)
    {
        if (hash_table[i] == 0)
            continue;
        HuffmanNode *temp = new HuffmanNode('\0', hash_table[i]);

        temp->value = static_cast<char>(i - 128);
        // cout<<temp->value<<endl;

        HuffmanTree[Huffmansize] = temp;
        Huffmansize++;
    }
}

// Load the Huffman tree from the frequency table
// HuffmanTree: array of Huffman tree nodes
// Huffmansize: size of the Huffman tree
void HuffmanTreeLoad2(HuffmanNode *HuffmanTree[], int Huffmansize)
{
    int num = 2, last;
    while (1)
    {
        if (2 * num - 1 == Huffmansize)
        {
            last = num - 2;
            break;
        }
        else if (2 * num - 1 < Huffmansize)
        {
            num *= 2;
            continue;
        }
        else
        {
            int a = Huffmansize - num + 1;
            a = (a + 1) / 2;
            last = num / 2 - 1 + a - 1;
            break;
        }
    }

    while (last > -1)
    {
        int oper = last;
        while (1)
        {
            int left = (oper + 1) * 2 - 1;
            int right = (oper + 1) * 2;

            if (left < Huffmansize)
            {
                if (right < Huffmansize)
                {
                    if (HuffmanTree[left]->frequency > HuffmanTree[right]->frequency)
                    {
                        if (HuffmanTree[oper]->frequency > HuffmanTree[right]->frequency)
                        {
                            HuffmanNode *change = HuffmanTree[oper];
                            HuffmanTree[oper] = HuffmanTree[right];
                            HuffmanTree[right] = change;
                            oper = right;
                            continue;
                        }
                    }
                }
                if (HuffmanTree[oper]->frequency > HuffmanTree[left]->frequency)
                {
                    HuffmanNode *change = HuffmanTree[oper];
                    HuffmanTree[oper] = HuffmanTree[left];
                    HuffmanTree[left] = change;
                    oper = left;
                    continue;
                }
            }
            else
                break;

            break;
        }
        last--;
    }
}

// Update the heap
// HuffmanTree: array of Huffman tree nodes
// Huffmansize: size of the Huffman tree
void Heap_Update(HuffmanNode *HuffmanTree[], int Huffmansize)
{
    if (Huffmansize <= 1)
    {
        return;
    }

    int oper = 0;
    while (1)
    {
        int left = (oper + 1) * 2 - 1;
        int right = (oper + 1) * 2;

        if (left < Huffmansize)
        {
            if (right < Huffmansize)
            {
                if (HuffmanTree[left]->frequency > HuffmanTree[right]->frequency)
                {
                    if (HuffmanTree[oper]->frequency > HuffmanTree[right]->frequency)
                    {
                        HuffmanNode *change = HuffmanTree[oper];
                        HuffmanTree[oper] = HuffmanTree[right];
                        HuffmanTree[right] = change;
                        oper = right;
                        continue;
                    }
                }
            }
            if (HuffmanTree[oper]->frequency > HuffmanTree[left]->frequency)
            {
                HuffmanNode *change = HuffmanTree[oper];
                HuffmanTree[oper] = HuffmanTree[left];
                HuffmanTree[left] = change;
                oper = left;
                continue;
            }
        }
        else
            break;

        break;
    }
}

// Build the Huffman tree
// HuffmanTree: array of Huffman tree nodes
// Huffmansize: size of the Huffman tree
void HuffmanTreeBuild(HuffmanNode *HuffmanTree[], int Huffmansize)
{
    while (Huffmansize > 1)
    {
        HuffmanNode *temp1 = HuffmanTree[0];
        HuffmanTree[0] = HuffmanTree[Huffmansize - 1];
        Huffmansize--;
        Heap_Update(HuffmanTree, Huffmansize);
        HuffmanNode *temp2 = HuffmanTree[0];
        HuffmanNode *temp_root = new HuffmanNode('\0', temp1->frequency + temp2->frequency);
        temp_root->flag = false;
        temp_root->left = temp1;
        temp_root->right = temp2;
        HuffmanTree[0] = temp_root;
        Heap_Update(HuffmanTree, Huffmansize);
    }
}

// Global array for storing Huffman codes
vector<string> char_Huff[256];

// Transfer the Huffman tree to a code table
// n: current node in the Huffman tree
// s: current Huffman code
void HuffmanTransfer(HuffmanNode *n, string s)
{
    if (n->flag)
    {
        int m = static_cast<int>(n->value) + 128;
        char_Huff[m].push_back(s);
    }

    if (n->left != nullptr)
    {
        HuffmanTransfer(n->left, s + '0');
        HuffmanTransfer(n->right, s + '1');
    }
}

// Write the encoded data to the output file
void writedat()
{
    if (!Huffdat)
    {
        cerr << "File Huffdat could not be open!" << endl;
        exit(1);
    }
    Huffdat.seekg(0, ios::beg);

    if (!HuffTree)
    {
        cerr << "File HuffTree could not be open!" << endl;
        exit(1);
    }
    HuffTree.seekg(0, ios::beg);

    Operation.open(file_name, ios::in | ios::binary);
    Operation.seekg(0, ios::beg);

    char a;
    unsigned char bit_set8;
    int bit_count = 0;
    int byte_amount = 0;

    while (Operation.read(reinterpret_cast<char *>(&a), sizeof(a)))
    {

        int n = static_cast<int>(a) + 128;

        for (int i = 0; i < char_Huff[n][0].length(); i++)
        {
            bit_set8 = (bit_set8 << 1) | (char_Huff[n][0][i] - '0');
            bit_count++;

            // cout<<"b"<<bit_count<<endl;

            if (bit_count == 8)
            {
                Huffdat.write(reinterpret_cast<const char *>(&bit_set8), sizeof(bit_set8));
                bit_set8 = 0;
                bit_count = 0;
                byte_amount++;
            }
        }
    }

    if (bit_count)
    {
        bit_set8 <<= (8 - bit_count);
        Huffdat.write(reinterpret_cast<const char *>(&bit_set8), sizeof(bit_set8));
        byte_amount++;
    }

    for (int i = 0; i < 256; i++)
    {
        int num = 0;
        if (char_Huff[i].size())
        {
            num = char_Huff[i][0].length();
        }
        HuffTree.write(reinterpret_cast<const char *>(&num), sizeof(num));
    }

    HuffTree.write(reinterpret_cast<const char *>(&bit_count), sizeof(bit_count));
    HuffTree.write(reinterpret_cast<const char *>(&byte_amount), sizeof(byte_amount));

    bit_set8 = 0;
    bit_count = 0;

    for (int i = 0; i < 256; i++)
    {
        if (char_Huff[i].size())
        {
            for (int j = 0; j < char_Huff[i][0].length(); j++)
            {
                bit_set8 = (bit_set8 << 1) | (char_Huff[i][0][j] - '0');
                bit_count++;

                if (bit_count == 8)
                {
                    HuffTree.write(reinterpret_cast<const char *>(&bit_set8), sizeof(bit_set8));
                    bit_set8 = 0;
                    bit_count = 0;
                }
            }
        }
    }

    if (bit_count)
    {
        bit_set8 <<= (8 - bit_count);
        HuffTree.write(reinterpret_cast<const char *>(&bit_set8), sizeof(bit_set8));
    }

    Huffdat.close();
    HuffTree.close();
    Operation.close();
}

// Perform the entire encoding process
void encode()
{
    string s;
    cout << "Enter the file name you want to operate(Default: Operation.txt): \n";
    cin >> s;
    if (s == "Default")
    {
        s = "Operation.txt";
    }
    file_name = s;

    Operation.open(s, ios::in | ios::binary);
    cout << "Enter the file name you want to save the compressed file(Default: Huff.bin): \n";
    cin >> s;
    if (s == "Default")
    {
        s = "Huff.bin";
    }
    Huffdat.open(s, ios::out | ios::binary);

    cout << "Enter the file name you want to save the tree(Default: Tree.bin): \n";
    cin >> s;
    if (s == "Default")
    {
        s = "Tree.bin";
    }
    HuffTree.open(s, ios::out | ios::binary);

    symbolCounts1(Operation, hash_table);

    HuffmanTreeLoad(HuffmanTree, hash_table);

    HuffmanTreeLoad2(HuffmanTree, Huffmansize);

    HuffmanTreeBuild(HuffmanTree, Huffmansize);

    HuffmanTransfer(HuffmanTree[0], "");

    writedat();

    cout << endl;

    cout << "If you want to see the Huffman code of each character, please enter 1 else enter 0: \n";

    int a;
    cin >> a;
    if (a == 1)
    {
        for (int i = 0; i < 256; i++)
        {
            if (char_Huff[i].size() == 0)
                continue;

            cout << i - 128 << "  ";
            if (char_Huff[i].size())
            {
                cout << char_Huff[i][0] << endl;
            }
        }
    }

    cout << "The compressed file and the tree file have been saved successfully!\n";
}

#endif