package hello.payqr.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import com.google.zxing.*;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import org.springframework.stereotype.Service;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;
import org.bytedeco.opencv.opencv_core.Mat;

import java.awt.image.BufferedImage;
import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class PayQrScannerService {

    public void startScanning() throws IOException {
        VideoCapture camera = new VideoCapture(0);
        Mat mat = new Mat();

        OpenCVFrameConverter.ToMat converterToMat = new OpenCVFrameConverter.ToMat();
        Java2DFrameConverter converterToImage = new Java2DFrameConverter();


        if (!camera.isOpened()) {
            log.error("❌ 카메라를 열 수 없습니다.");
            return;
        }

        log.info("📷 QR 스캐너 시작됨");

        while (camera.read(mat)) {
            Frame frame = converterToMat.convert(mat);
            BufferedImage bufferedImage = converterToImage.convert(frame);

            if (bufferedImage == null) continue;

            LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            try {
                Result result = new MultiFormatReader().decode(bitmap);
                log.info("✅ QR 인식됨: {}", result.getText());
            } catch (NotFoundException e) {
                // QR 없음
            }
        }


        camera.release();
        log.info("📷 QR 스캐너 종료됨");
    }
}
