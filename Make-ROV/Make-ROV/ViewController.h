//
//  ViewController.h
//  Make-ROV
//
//  Created by testing on 16/2/2015.
//  Copyright (c) 2015年 pcms.ROVteam. All rights reserved.
//

#import <Cocoa/Cocoa.h>

@interface ViewController : NSViewController{
	IBOutlet NSTextField *_welcomeLabel;
	IBOutlet NSButton *_welcomeContinuebtn;
	
	NSImage *_welcomeImage;
	IBOutlet NSImageView *_welcomeImageView;
	
}
-(void)deleteAllViews;


@end

