#import <Foundation/Foundation.h>
#import "DouYin.h"
#import <DouyinOpenSDK/DouyinOpenSDKAuth.h>
#import <UIKit/UIKit.h>
@interface dyModule ()

@end

@implementation dyModule
// 这句代码是必须的 用来导出 module, 这样才能在 RN 中访问 nativeModule这个 module
RCT_EXPORT_MODULE();

// 接收字符
RCT_EXPORT_METHOD(sendDyRequest:(NSString *)name location:(NSString *)location callback:(RCTResponseSenderBlock)callback)
{
  DouyinOpenSDKAuthRequest *request = [[DouyinOpenSDKAuthRequest alloc] init];
  request.permissions = [NSOrderedSet orderedSetWithObject:@"user_info"];
  //可选附加权限（如有），用户可选择勾选/不勾选
//    request.additionalPermissions = [NSOrderedSet orderedSetWithObjects:@{@"permission":@"friend_relation",@"defaultChecked":@"1"}, @{@"permission":@"message",@"defaultChecked":@"0"}, nil];
  __weak typeof(self) ws = self;
  
  dispatch_async(dispatch_get_main_queue(), ^{
    
    UIViewController *vc =  [UIApplication sharedApplication].keyWindow.rootViewController;
    [request sendAuthRequestViewController:vc completeBlock:^(DouyinOpenSDKAuthResponse * _Nonnull resp) {
      NSLog(@"userID: %@", resp.code);
     
      if(resp.errCode == 0){
        callback(@[resp.code]);
      }
//          __strong typeof(ws) sf = ws;
//          NSString *alertString = nil;
//   if (resp.errCode == 0) {
//              alertString = [NSString stringWithFormat:@"Author Success Code : %@, permission : %@",resp.code, resp.grantedPermissions];
//          } else{
//              alertString = [NSString stringWithFormat:@"Author failed code : %@, msg : %@",@(resp.errCode), resp.errString];
//          }
//  //        [UIAlertController showMsg:alertString inVC:sf];
      }];
//
    
  });
  
//  [request sendAuthRequestViewController:ws completeBlock:^(DouyinOpenSDKAuthResponse * _Nonnull resp) {
//      __strong typeof(ws) sf = ws;
//      NSString *alertString = nil;
//      if (resp.errCode == 0) {
//          alertString = [NSString stringWithFormat:@"Author Success Code : %@, permission : %@",resp.code, resp.grantedPermissions];
//      } else{
//          alertString = [NSString stringWithFormat:@"Author failed code : %@, msg : %@",@(resp.errCode), resp.errString];
//      }
//
//  }];
}
@end
