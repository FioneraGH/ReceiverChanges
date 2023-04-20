# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.18.1)

set(CMAKE_VERBOSE_MAKEFILE on)

# 支持gnu++11
#set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11")

message(CMAKE_BUILD_TYPE: ${CMAKE_BUILD_TYPE})
message(CMAKE_CXX_FLAGS: ${CMAKE_CXX_FLAGS})

set(PATH_TO_PREBUILT ${CMAKE_SOURCE_DIR}/../jniLibs)
set(PATH_TO_FFMPEG ${CMAKE_SOURCE_DIR}/third_party/ffmpeg/include)
set(PATH_TO_UTIL ${CMAKE_SOURCE_DIR}/util)

# Declares and names the project.

project("radar")

include_directories(
        ${PATH_TO_FFMPEG}
        ${PATH_TO_UTIL}
)

message(LINK_LIBRARY: ${PATH_TO_PREBUILT}/${ANDROID_ABI})

link_directories(
        ${PATH_TO_PREBUILT}/${ANDROID_ABI}
)

file(GLOB SRC_FILES ${CMAKE_SOURCE_DIR}/*.cpp)

file(GLOB SRC_FILES_MEDIA ${CMAKE_SOURCE_DIR}/media/*.cpp)
file(GLOB SRC_FILES_DECODER ${CMAKE_SOURCE_DIR}/media/decoder/*.cpp)
file(GLOB SRC_FILES_V_DECODER ${CMAKE_SOURCE_DIR}/media/decoder/video/*.cpp)

file(GLOB SRC_FILES_RENDER ${CMAKE_SOURCE_DIR}/media/render/*.cpp)
file(GLOB SRC_FILES_V_RENDER ${CMAKE_SOURCE_DIR}/media/render/video/*.cpp)
file(GLOB SRC_FILES_NATIVE_V_RENDER ${CMAKE_SOURCE_DIR}/media/render/video/native_render/*.cpp)

add_library(
        radar
        SHARED
        ${PATH_TO_UTIL}
        ${CMAKE_SOURCE_DIR}/util/logger.h
        ${CMAKE_SOURCE_DIR}/util/timer.c
        ${SRC_FILES}
        ${SRC_FILES_MEDIA}
        ${SRC_FILES_DECODER}
        ${SRC_FILES_V_DECODER}
        ${SRC_FILES_V_RENDER}
        ${SRC_FILES_NATIVE_V_RENDER}
)

target_link_libraries(
        radar

#        avformat
#        avcodec
#        avfilter
#        swresample
#        swscale
#        avutil
        ffmpeg

        android
        EGL
        GLESv3
        OpenSLES
        log
        m
        z
)
