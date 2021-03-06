#import "CDVHeartBeat.h"
#import "CDVHeartBeatDetection.h"

@interface CDVHeartBeat()<CDVHeartBeatDetectionDelegate>

@property (nonatomic, assign) bool detecting;
@property (nonatomic, strong) NSMutableArray *bpms;

@end

@implementation CDVHeartBeat


- (void)take:(CDVInvokedUrlCommand*)command
{
    [self.commandDelegate runInBackground: ^{
    
        NSString* callbackId = [command callbackId];
        
        CDVHeartBeatDetection* heartBeatDetection = [[CDVHeartBeatDetection alloc] init];
        heartBeatDetection.delegate = self;
        self.detecting = true;
        [heartBeatDetection startDetection];
        
        while(self.detecting){
            
        }
        
        [self.bpms sortedArrayUsingDescriptors:@[[NSSortDescriptor sortDescriptorWithKey:@"self" ascending:YES]]];
        
        int bpm = [((NSNumber*)self.bpms[self.bpms.count/2]) intValue];
        
        NSString* msg = [NSString stringWithFormat: @"%i", bpm];
        CDVPluginResult* result = [CDVPluginResult
                                   resultWithStatus:CDVCommandStatus_OK
                                   messageAsString:msg];
        
        [self.commandDelegate sendPluginResult:result callbackId:callbackId];
    
    }];
}

- (void)heartRateStart{
    self.bpms = [[NSMutableArray alloc] init];
}

- (void)heartRateUpdate:(int)bpm atTime:(int)seconds{
    [self.bpms addObject:[NSNumber numberWithInt:bpm]];}

- (void)heartRateEnd{
    self.detecting = false;
}

@end