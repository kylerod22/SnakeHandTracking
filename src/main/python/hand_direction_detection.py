import cv2 as cv
import time
import numpy as np
import hand_tracking_module as htm

init_x_list = np.zeros(21, dtype=int)
init_y_list = np.zeros(21, dtype=int)

wCam, hCam = 640, 480

prev_time = 0
curr_time = 0

threshold = 18000

id = 1
cam = cv.VideoCapture(id)
cam.set(3, wCam)
cam.set(4, hCam)

direction = ""
lastDir = ""

detector = htm.HandDetector(max_hands=1, detect_conf=0.7)
while True:
    success, img = cam.read()
    img = detector.find_hands(img)
    landmark_list = detector.find_positions(img, draw=False)

    curr_time = time.time()
    fps = 1 / (curr_time - prev_time)
    prev_time = curr_time

    if len(landmark_list) > 0:
        total_x = 0
        total_y = 0
        for id, landmark in enumerate(landmark_list):
            total_x += (landmark[1] - init_x_list[id])
            total_y += (landmark[2] - init_y_list[id])
            init_x_list[id] = landmark[1]
            init_y_list[id] = landmark[2]

        dx = total_x // (1 / fps)
        dy = -total_y // (1 / fps)

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
        if lastDir != direction:
            print(direction, flush=True)
            lastDir = direction

            # print(dx, dy)
        # print("--------")    

    cv.putText(img, str(int(fps)), (10, 70), cv.FONT_HERSHEY_COMPLEX, 1, (255, 0, 255), 3)
    cv.imshow("Video", img)
    if cv.waitKey(1) & 0xFF == ord('q'):
        break
cam.release()
cv.destroyAllWindows()
