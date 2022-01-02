import cv2 as cv
import mediapipe as mp
import time


class HandDetector:
    def __init__(self, mode=False, max_hands=2, complexity=1, detect_conf=0.5, track_conf=0.5):
        self.mode = mode
        self.maxHands = max_hands
        self.complex = complexity
        self.detectionConf = detect_conf
        self.trackingConf = track_conf

        self.mpHands = mp.solutions.hands  # Grabs hand solution
        self.hands = self.mpHands.Hands(self.mode, self.maxHands, self.complex, self.detectionConf,
                                        self.trackingConf)  # (static_image_mode, max_hands, min_detect_conf, min_track_conf)
        self.mpDraw = mp.solutions.drawing_utils

    def find_hands(self, img, draw=True):
        img_RGB = cv.cvtColor(img, cv.COLOR_BGR2RGB)  # Convert to RGB before processing
        self.results = self.hands.process(img_RGB)

        if self.results.multi_hand_landmarks:  # If hand is detected
            for handLandmarks in self.results.multi_hand_landmarks:  # Get the hand landmarks
                if draw:
                    self.mpDraw.draw_landmarks((img), handLandmarks, self.mpHands.HAND_CONNECTIONS)
        return img

    def find_positions(self, img, hand_num=0, draw=True):
        landmark_list = []
        if self.results.multi_hand_landmarks:  # If hand is detected
            my_hand = self.results.multi_hand_landmarks[hand_num]
            for id, lm in enumerate(my_hand.landmark):  # Iterate through each individual landmark
                h, w, c = img.shape
                landmark_x, landmark_y = int(lm.x * w), int(lm.y * h)  # Convert into on-screen coords
                # print(id, landmark_x, landmark_y)
                # cv.putText(img, str(id), (landmark_x + 10, landmark_y), cv.FONT_HERSHEY_COMPLEX, 0.7, (0,0,0))
                landmark_list.append([id, landmark_x, landmark_y])

                if draw:
                    cv.circle(img, (landmark_x, landmark_y), 7, (255, 0, 255), cv.FILLED)
        return landmark_list


def main():
    prev_time = 0
    curr_time = 0
    id = 0
    cam = cv.VideoCapture(id)
    detector = HandDetector()
    while True:
        success, img = cam.read()
        img = detector.find_hands(img)
        landmark_list = detector.find_positions(img, draw=False)
        if len(landmark_list) > 0:
            print(landmark_list[0])
        curr_time = time.time()
        fps = 1 / (curr_time - prev_time)
        prev_time = curr_time

        cv.putText(img, str(int(fps)), (10, 70), cv.FONT_HERSHEY_COMPLEX, 1, (255, 0, 255), 3)
        cv.imshow("Video", img)
        if cv.waitKey(1) & 0xFF == ord('q'):
            break
    cam.release()
    cv.destroyAllWindows()


if __name__ == "__main__":
    main()
