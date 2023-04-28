project(OcrLite)

cmake_minimum_required(VERSION 3.4.1)

option(OCR_LITE_VULKAN "OcrLite Enable Vulkan" ON)
#set(OCR_LITE_VULKAN ON)
if (OCR_LITE_VULKAN)
    set(ncnn_DIR "${CMAKE_SOURCE_DIR}/ncnn-vulkan/${ANDROID_ABI}/lib/cmake/ncnn")
else ()
    set(ncnn_DIR "${CMAKE_SOURCE_DIR}/ncnn/${ANDROID_ABI}/lib/cmake/ncnn")
endif ()
find_package(ncnn REQUIRED)

aux_source_directory(. CURRENT_DIR)
include_directories(${CMAKE_SOURCE_DIR}/opencv/include/)
add_library(
        OcrLite
        SHARED
        ${CURRENT_DIR}
)

add_library(libopencv_java4 STATIC IMPORTED)
set_target_properties(
        libopencv_java4
        PROPERTIES IMPORTED_LOCATION
        ${CMAKE_SOURCE_DIR}/opencv/${ANDROID_ABI}/libopencv_java4.so
)

target_link_libraries(
        OcrLite
        libopencv_java4
        ncnn
)
if (OCR_LITE_VULKAN)
    target_compile_definitions(OcrLite PRIVATE __VULKAN__)
endif ()