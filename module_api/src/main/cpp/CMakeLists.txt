# 指定了CMake的最低版本要求
cmake_minimum_required(VERSION 3.10.2)
project("KoMmApp")
# 将C++标准设置为C++17。
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++17")
# 编译出一个动态库 native，源文件只有 src/main/cpp/native.cpp
add_library(
        native #库名字
        SHARED #共享库
        native.cpp #要构建的cpp文件名
)
include_directories(${CMAKE_CURRENT_SOURCE_DIR}/include/)
# 查找log库
find_library(log-lib log)
# 将native-lib库与log-lib库进行链接
target_link_libraries(native ${log-lib})