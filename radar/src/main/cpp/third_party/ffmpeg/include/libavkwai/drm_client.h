//
// Created by master on 2020/8/12.
//

#ifndef SECURITYAIO_DRM_CLIENT__H
#define SECURITYAIO_DRM_CLIENT__H

#include <stdint.h>
#include <string.h>

typedef uint8_t             u1;
typedef uint16_t            u2;
typedef uint32_t            u4;
typedef uint64_t            u8;
typedef int8_t              s1;
typedef int16_t             s2;
typedef int32_t             s4;
typedef int64_t             s8;

enum DRMCommand {
    DRM_CREATE_CONTEXT_ASYNC = 1,
    DRM_CREATE_CONTEXT_SYNC,
    DRM_DECRYPT_STREAM,
    DRM_DESTROY_CONTEXT
};

/// 对外失败错误码定义
#define DRM_INTERFACE_INIT_CALLBACK_SUCC 0   // 网络拉取mainkey成功，回调状态成功
#define DRM_INTERFACE_INIT_CALLBACK_DRMSERVER_MAINKEY_ERROR -3 // drmserver resp statuscode 为0 但是主密钥为空
#define DRM_INTERFACE_INIT_CALLBACK_DRMSERVER_RESP_FAIL -4 // drmserver resp 返回fail
#define DRM_INTERFACE_INIT_CALLBACK_DRMSERVER_RESP_AUTHOTKEN_FAIL -5 // drmserver resp auth token fail
#define DRM_INTERFACE_INIT_CALLBACK_DRMINNER_ERROR -6 // drmserver inner error (统一的错误码：包含其它错误码)

// external function pointer type
typedef int (*DRM_CREATE_FP_CALLBACK)(void *, int);
typedef int (*DRM_DOCOMMAND_FP)(int command, ...);

typedef struct DrmCb {
    int (*callback)(void*,int);
    void *opaque;
} DrmCb;

//
#define TOKENLEN 16
__attribute__ ((visibility("default"))) long long getDRMBridgeFuncAddr();
__attribute__ ((visibility("default"))) void drmServerHttpCallback(unsigned int randomId, const char *tudplp, unsigned char *encData, int encDataLen, int errorcode);

#endif //SECURITYAIO_DRM_CLIENT__H
