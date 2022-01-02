import cv2 as cv
import mediapipe as mp
import time


class handDetector():
    def __init__(self, mode=False, maxHands=2, complex=1, detectionConf=0.5, trackingConf=0.5):
        self.mode = mode
        self.maxHands = maxHands
        self.complex = complex
        self.detectionConf = detectionConf
        self.trackingConf = trackingConf

        self.mpHands = mp.solutions.hands #Grabs hand solution
        self.hands = self.mpHands.Hands(self.mode, self.maxHands, self.complex, self.detectionConf, self.trackingConf) #(static_image_mode, max_hands, min_detect_conf, min_track_conf)
        self.mpDraw = mp.solutions.drawing_utils
    
    def findHands(self, img, draw=True):
        img_RGB = cv.cvtColor(img, cv.COLOR_BGR2RGB) #Convert to RGB before processing
        self.results = self.hands.process(img_RGB)

        if self.results.multi_hand_landmarks: #If hand is detected
            for handLandmarks in self.results.multi_hand_landmarks: #Get the hand landmarks
                if draw:
                    self.mpDraw.draw_landmarks((img), handLandmarks, self.mpHands.HAND_CONNECTIONS)
        return img

                

    def findPositions(self, img, handNum=0, draw=True):
        landmarkList = []
        if self.results.multi_hand_landmarks: #If hand is detected
            myHand = self.results.multi_hand_landmarks[handNum]
            for id, lm in enumerate(myHand.landmark): #Iterate through each individual landmark
                h, w, c = img.shape
                landmarkX, landmarkY = int(lm.x * w), int(lm.y * h) #Convert into on-screen coords
                #print(id, landmarkX, landmarkY)
                #cv.putText(img, str(id), (landmarkX + 10, landmarkY), cv.FONT_HERSHEY_COMPLEX, 0.7, (0,0,0))
                landmarkList.append([id, landmarkX, landmarkY])    

                if draw:
                    cv.circle(img, (landmarkX, landmarkY), 7, (255,0,255), cv.FILLED)
        return landmarkList            

def main():
    prevTime = 0
    currTime = 0
    id = 0
    cam = cv.VideoCapture(id)
    detector = handDetector()
    while True:
        success, img = cam.read()
        img = detector.findHands(img)
        landmarkList = detector.findPositions(img, draw=False)
        if len(landmarkList) > 0:
            print(landmarkList[0])
        currTime = time.time()
        fps = 1/(currTime - prevTime)
        prevTime = currTime

        cv.putText(img, str(int(fps)), (10,70), cv.FONT_HERSHEY_COMPLEX, 1, (255,0,255), 3)
        cv.imshow("Video", img)
        if cv.waitKey(1) & 0xFF == ord('q'):
            break
    cam.release()
    cv.destroyAllWindows()

if __name__ == "__main__":
    main()    