package com.vocketlist.android.helper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.flipboard.bottomsheet.BottomSheetLayout;
import com.flipboard.bottomsheet.commons.MenuSheetView;
import com.gonbro.memovie.R;
import com.kbeanie.multipicker.api.CameraImagePicker;
import com.kbeanie.multipicker.api.CameraVideoPicker;
import com.kbeanie.multipicker.api.FilePicker;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.VideoPicker;
import com.kbeanie.multipicker.api.callbacks.FilePickerCallback;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.callbacks.VideoPickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenFile;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.kbeanie.multipicker.api.entity.ChosenVideo;

import java.util.List;

/**
 * 헬퍼 : 첨부
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 13.
 */
public class AttachmentHelper {
    private static final String TAG = AttachmentHelper.class.getSimpleName();

    private static final String PICKER_PATH = "picker_path";

    private BottomSheetLayout bottomSheetLayout;
    private MenuSheetView menuSheetView;

    private ImagePicker imagePicker;
    private VideoPicker videoPicker;
    private CameraImagePicker cameraImagePicker;
    private CameraVideoPicker cameraVideoPicker;
    private FilePicker filePicker;

    private String outputPath;

    private List<ChosenImage> images;
    private List<ChosenVideo> videos;
    private List<ChosenFile> files;

    private Activity act;

    private boolean isAllowMultiple;
    private boolean debug = true;

    public interface PickerCallback {
        void onImagesChosen(List<ChosenImage> list);
        void onVideosChosen(List<ChosenVideo> list);
        void onFilesChosen(List<ChosenFile> list);
        void onError(String s);
    }

    // 이벤트
    private MenuSheetView.OnMenuItemClickListener onMenuItemClickListener = new MenuSheetView.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int id = item.getItemId();

            switch (id) {
                case R.id.action_voice: doVoice(); break;
                case R.id.action_video: doVideo(); break;
                case R.id.action_camera: doCamera(); break;
                case R.id.action_album_video: doAlbumVideo(); break;
                case R.id.action_album_photo: doAlbumPhoto(); break;
                case R.id.action_file: doFile(); break;
                default: return false;
            }

            //
            bottomSheetLayout.dismissSheet();
            return true;
        }
    };

    private ImagePickerCallback imagePickerCallback = new ImagePickerCallback() {
        @Override
        public void onImagesChosen(List<ChosenImage> list) {
            images = list;

            if (pickerCallback != null) pickerCallback.onImagesChosen(list);
        }

        @Override
        public void onError(String s) {
            if (pickerCallback != null) pickerCallback.onError(s);
        }
    };

    private VideoPickerCallback videoPickerCallback = new VideoPickerCallback() {
        @Override
        public void onVideosChosen(List<ChosenVideo> list) {
            videos = list;

            if (pickerCallback != null) pickerCallback.onVideosChosen(list);
        }

        @Override
        public void onError(String s) {
            if (pickerCallback != null) pickerCallback.onError(s);
        }
    };

    private FilePickerCallback filePickerCallback = new FilePickerCallback() {
        @Override
        public void onFilesChosen(List<ChosenFile> list) {
            files = list;

            if (pickerCallback != null) pickerCallback.onFilesChosen(list);
        }

        @Override
        public void onError(String s) {
            if (pickerCallback != null) pickerCallback.onError(s);
        }
    };

    private PickerCallback absPickerCallback = new PickerCallback() {
        @Override
        public void onImagesChosen(List<ChosenImage> list) {
        }

        @Override
        public void onVideosChosen(List<ChosenVideo> list) {
        }

        @Override
        public void onFilesChosen(List<ChosenFile> list) {
        }

        @Override
        public void onError(String s) {
        }
    };

    private PickerCallback pickerCallback = absPickerCallback;

    /**
     * 생성자
     *
     * @param bottomSheetLayout
     */
    public AttachmentHelper(Activity act, BottomSheetLayout bottomSheetLayout) {
        this.bottomSheetLayout = bottomSheetLayout;
        this.act = act;

        bottomSheetLayout.setPeekOnDismiss(true);
        menuSheetView = new MenuSheetView(act, MenuSheetView.MenuType.GRID, R.string.file, onMenuItemClickListener);
        menuSheetView.inflateMenu(R.menu.attachments);
    }

    public ImagePicker getImagePicker() {
        return imagePicker;
    }

    public VideoPicker getVideoPicker() {
        return videoPicker;
    }

    public FilePicker getFilePicker() {
        return filePicker;
    }

    public List<ChosenImage> getImages() {
        return images;
    }

    public List<ChosenVideo> getVideos() {
        return videos;
    }

    public List<ChosenFile> getFiles() {
        return files;
    }

    /**
     * 콜백
     *
     * @param pickerCallback
     */
    public void setPickerCallback(PickerCallback pickerCallback){
        this.pickerCallback = pickerCallback;
    }

    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK) {
            // 사진 앰범
            if(requestCode == Picker.PICK_IMAGE_DEVICE) {
                if(imagePicker == null) {
                    imagePicker = new ImagePicker(act);
                    imagePicker.setImagePickerCallback(imagePickerCallback);
                }
                imagePicker.submit(data);
            }
            // 동영상 앨범
            else if(requestCode == Picker.PICK_VIDEO_DEVICE) {
                if(videoPicker == null) {
                    videoPicker = new VideoPicker(act);
                    videoPicker.setVideoPickerCallback(videoPickerCallback);
                }
                videoPicker.submit(data);
            }
            // 사진 촬영
            else if(requestCode == Picker.PICK_IMAGE_CAMERA) {
                if(cameraImagePicker == null) {
                    cameraImagePicker = new CameraImagePicker(act, outputPath);
                    cameraImagePicker.setImagePickerCallback(imagePickerCallback);
                }
                cameraImagePicker.submit(data);
            }
            // 동영상 촬영
            else if(requestCode == Picker.PICK_VIDEO_CAMERA) {
                if(cameraVideoPicker == null) {
                    cameraVideoPicker = new CameraVideoPicker(act, outputPath);
                    cameraVideoPicker.setVideoPickerCallback(videoPickerCallback);
                }
                cameraVideoPicker.submit(data);
            }
            // 파일
            else if (requestCode == Picker.PICK_FILE) {
                if(filePicker == null) {
                    filePicker = new FilePicker(act);
                    filePicker.setFilePickerCallback(filePickerCallback);
                }
                filePicker.submit(data);
            }
        }

        return true;
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putString(PICKER_PATH, outputPath);

    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(PICKER_PATH)) {
                outputPath = savedInstanceState.getString(PICKER_PATH);
            }
        }
    }

    /**
     * 보이기
     */
    public void show() {
        if (bottomSheetLayout.isSheetShowing()) bottomSheetLayout.dismissSheet();
        else bottomSheetLayout.showWithSheetView(menuSheetView);
    }

    /**
     * 다중선태
     */
    public void setAllowMuliple(boolean allowMultiple) {
        isAllowMultiple = allowMultiple;
    }

    /**
     * TODO 음성녹음
     */
    public void doVoice() {

    }

    /**
     * 동영상 촬영
     */
    public void doVideo() {
        cameraVideoPicker = new CameraVideoPicker(act);
        cameraVideoPicker.setDebugglable(debug);
        cameraVideoPicker.setVideoPickerCallback(videoPickerCallback);
        // videoPicker.shouldGenerateMetadata(false); // Default is true
        // videoPicker.shouldGenerateThumbnails(false); // Default is true
        outputPath = cameraVideoPicker.pickVideo();
    }

    /**
     * 사진 촬영
     */
    public void doCamera() {
        cameraImagePicker = new CameraImagePicker(act);
        cameraImagePicker.setDebugglable(debug);
        cameraImagePicker.setImagePickerCallback(imagePickerCallback);
        // imagePicker.shouldGenerateMetadata(false); // Default is true
        // imagePicker.shouldGenerateThumbnails(false); // Default is true
        outputPath = cameraImagePicker.pickImage();
    }

    /**
     * 동영상 앨범
     */
    public void doAlbumVideo() {
        videoPicker = new VideoPicker(act);
        videoPicker.setDebugglable(debug);
        videoPicker.setVideoPickerCallback(videoPickerCallback);
        if(isAllowMultiple) videoPicker.allowMultiple(); // Default is false
        // videoPicker.shouldGenerateMetadata(false); // Default is true
        // videoPicker.shouldGeneratePreviewImages(false); // Default is true
        videoPicker.pickVideo();
    }

    /**
     * 사진 앨범
     */
    public void doAlbumPhoto() {
        imagePicker = new ImagePicker(act);
        imagePicker.setDebugglable(debug);
        imagePicker.setImagePickerCallback(imagePickerCallback);
        if(isAllowMultiple) imagePicker.allowMultiple(); // Default is false
        // imagePicker.shouldGenerateMetadata(false); // Default is true
        // imagePicker.shouldGenerateThumbnails(false); // Default is true
        imagePicker.pickImage();
    }

    /**
     * 파일첨부
     */
    public void doFile() {
        filePicker = new FilePicker(act);
        filePicker.setDebugglable(debug);
        filePicker.setFilePickerCallback(filePickerCallback);
        // filePicker.allowMultiple();
        filePicker.pickFile();
    }


}
