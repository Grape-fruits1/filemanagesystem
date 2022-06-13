#define EX_PORT
#include "library.h"
#include <iostream>
#include <windows.h>
#include <conio.h>
#include <vector>

void setX(COORD& a, short x) {
    a.X = x;
    SetConsoleCursorPosition(GetStdHandle(STD_OUTPUT_HANDLE), a);
}

void setY(COORD& a, short y) {
    a.Y = y;
    SetConsoleCursorPosition(GetStdHandle(STD_OUTPUT_HANDLE), a);
}

const char* start(std::string fileName) {
    COORD a = { 0, 0 };
    std::string buf = "";
    //每一行的长度
    std::vector<short> lengths;
    lengths.push_back(0);
    //当前光标所在行
    int row = 0;
    //总行数
    int totalRows = 0;
    CONSOLE_SCREEN_BUFFER_INFO csbi;
    while (true) {
        int command = _getch();
        //编辑模式，按i进入
        if (0x69 == command) {
            char ch = 0;
            char ch2 = 0;
            int pointer = 0;
            while (true) {
                //非esc
                if (0 == GetAsyncKeyState(VK_ESCAPE)) {
                    //退格
                    if (0 != GetAsyncKeyState(VK_BACK)) {
                        //返回上一行
                        if (row > 0 && lengths[row] == 0) {
                            row--;
                            setY(a, a.Y - 1);
                            setX(a, lengths[row]);
                            buf.pop_back();
                            std::cout << 1;
                        }
                        else if (lengths[row] > 0) {
                            std::cout << " \b";
                            lengths[row]--;
                            buf.pop_back();
                        }
                        /*if ((char)VK_RETURN == buf.back()) {

                        }
                        buf.pop_back();*/
                    }
                    ch = _getch();
                    //std::cout << (int) ch;
                    //回车
                    if (VK_RETURN == ch) {
                        system("cls");
                        lengths.insert(lengths.begin() + row + 1, lengths[row] - a.X);
                        lengths[row] = a.X;
                        row++;
                        totalRows++;
                        for (int i = 0; i < row; i++) {
                            pointer += lengths[i];
                        }
                        pointer -= (lengths[row - 1] - a.X);
                        buf.insert(pointer, 1, '\n');
                        std::cout << buf;
                        setX(a, 0);
                        setY(a, a.Y + 1);
                    }
                    if (-32 == ch) {
                        ch2 = _getch();
                    }
                    //左键
                    if ('K' == ch2) {
                        setX(a, a.X - 1);
                        continue;
                    }
                    //右键
                    if ('M' == ch2) {
                        if (a.X < lengths[row]) {
                            setX(a, a.X + 1);
                        }
                        continue;
                    }
                    //上键
                    if ('H' == ch2) {
                        if (row > 0 && lengths[row - 1] >= a.Y) {
                            setY(a, a.Y - 1);
                            row--;
                        }
                        else if (row == 0) {

                        }
                        else {
                            setY(a, lengths[row - 1]);
                            row--;
                        }
                        continue;
                    }
                    //下键
                    if ('P' == ch2) {
                        if (totalRows == a.Y) {
                            continue;
                        }
                        if (lengths[row + 1] >= a.Y) {
                            setY(a, a.Y + 1);
                            row++;
                        }
                        else {
                            setY(a, lengths[row + 1]);
                            row++;
                        }
                        continue;
                    }
                    std::cout << ch;
                    buf += ch;
                    lengths[row]++;
                    setX(a, a.X + 1);
                    //更新光标坐标
                    /*if (GetConsoleScreenBufferInfo(GetStdHandle(STD_OUTPUT_HANDLE), &csbi)) {
                        a.X = csbi.dwCursorPosition.X;
                        a.Y = csbi.dwCursorPosition.Y;
                    }*/
                }
                else {
                    const char* data = buf.data();
                    unsigned int size = buf.length();
                    //前四个byte为长度，请拼成一个int类型处理
                    unsigned char sizeByte[4];
                    sizeByte[0] = size >> 24;
                    sizeByte[1] = size >> 16;
                    sizeByte[2] = size >> 8;
                    sizeByte[3] = size;
                    char output[size + 4];
                    for (int i = 0; i < 4; i++) {
                       output[i] = sizeByte[i];
                    }
                    for (int i = 0; i < size; i++) {
                        output[i + 4] = data[i];
                    }
                    const char *outputByte = output;
                    return outputByte;
                }
            }
        }
        //命令模式
        if (0 != GetAsyncKeyState(0x3A)) {
//            return;
        }
    }
}
