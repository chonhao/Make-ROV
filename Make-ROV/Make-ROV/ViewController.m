//
//  ViewController.m
//  Make-ROV
//
//  Created by testing on 16/2/2015.
//  Copyright (c) 2015年 pcms.ROVteam. All rights reserved.
//

#import "ViewController.h"

@implementation ViewController

- (void)viewDidLoad {
	[super viewDidLoad];
	
	// Do any additional setup after loading the view.
	
	NSColor *backgroundColor = colorFromRGB(179,229,252);
	[self.view setWantsLayer:YES];
	[self.view.layer setBackgroundColor: [backgroundColor CGColor]];
	
	int _screenWidth = 1024;
	_welcomeLabel = [[NSTextField alloc]initWithFrame:NSMakeRect(0, 230, _screenWidth, 100)];
	[_welcomeLabel setStringValue:@"Make-ROV"];
	[_welcomeLabel setTextColor: [NSColor blackColor]];
	[_welcomeLabel setFont:[NSFont systemFontOfSize:56]];
	[_welcomeLabel setAlignment:NSCenterTextAlignment];
	[_welcomeLabel setBezeled:NO];
	[_welcomeLabel setDrawsBackground:NO];
	[_welcomeLabel setEditable:NO];
	[_welcomeLabel setSelectable:NO];
	[self.view addSubview:_welcomeLabel];
	
	
	_welcomeContinuebtn = [[NSButton alloc] initWithFrame:NSMakeRect(410, 100, 200, 60)];
	[_welcomeContinuebtn setTitle:@"Getting Started"];
	[_welcomeContinuebtn setButtonType:NSMomentaryPushInButton];
	[_welcomeContinuebtn setBezelStyle:NSRoundedBezelStyle];
	[_welcomeContinuebtn setFont: [NSFont systemFontOfSize:20]];
	[self.view addSubview:_welcomeContinuebtn];
	_welcomeContinuebtn.target = self;
	_welcomeContinuebtn.action = @selector(continueBtnClicked:);
	
	
	
}

- (void)setRepresentedObject:(id)representedObject {
	[super setRepresentedObject:representedObject];

	// Update the view, if already loaded.
}

- (void)deleteAllViews{
	[_welcomeLabel removeFromSuperview];
	[_welcomeContinuebtn removeFromSuperview];
}

- (IBAction)continueBtnClicked:(id)sender{
	NSLog(@"continueBtnClicked");
	[self deleteAllViews];
}

static NSColor *colorFromRGB(unsigned char r, unsigned char g, unsigned char b)
{
	return [NSColor colorWithCalibratedRed:(r/255.0f) green:(g/255.0f) blue:(b/255.0f) alpha:1.0];
}



@end
