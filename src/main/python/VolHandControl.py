import cv2 as cv
import time
import numpy as np
import HandTrackingModule as htm

initXList = np.zeros(21, dtype=int)
initYList = np.zeros(21, dtype=int)

wCam, hCam = 640, 480

prevTime = 0
currTime = 0

threshold = 12000

id = 0
cam = cv.VideoCapture(id)
cam.set(3, wCam)
cam.set(4, hCam)

direction = ""
lastDir = ""

detector = htm.handDetector(maxHands=1, detectionConf=0.7)
while True:
    success, img = cam.read()
    img = detector.findHands(img)
    landmarkList = detector.findPositions(img, draw=False)

    currTime = time.time()
    fps = 1/(currTime - prevTime)
    prevTime = currTime

    if len(landmarkList) > 0:
        totalX = 0
        totalY = 0
        for id, landmark in enumerate(landmarkList):
            totalX += (landmark[1] - initXList[id])
            totalY += (landmark[2] - initYList[id])
            initXList[id] = landmark[1]
            initYList[id] = landmark[2]
          
        dx = totalX // (1 / fps)
        dy = -totalY // (1 / fps)

        if abs(dx) > threshold or abs(dy) > threshold:
            if abs(dx) > abs(dy):
                if dx >= 0:
                    direction = "RIGHT"
                else:
                    direction = "LEFT"    
            else:
                if dy >= 0:
                    direction = "UP"
                else:
                    direction = "DOWN" 
        if (lastDir != direction):
            print(direction)
            lastDir = direction            
        
        # print(dx, dy)
        # print("--------")    
    

    cv.putText(img, str(int(fps)), (10,70), cv.FONT_HERSHEY_COMPLEX, 1, (255,0,255), 3)
    cv.imshow("Video", img)
    if cv.waitKey(1) & 0xFF == ord('q'):
        break
cam.release()
cv.destroyAllWindows()