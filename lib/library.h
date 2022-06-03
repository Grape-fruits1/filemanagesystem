#ifndef VI1_0_LIBRARY_H
#define VI1_0_LIBRARY_H

#include <string>

#ifdef EX_PORT
#else
#define EX_PORT __declspec(dllimport)
#endif

extern "C" EX_PORT const char* start(std::string fileName);

#endif //VI1_0_LIBRARY_H
