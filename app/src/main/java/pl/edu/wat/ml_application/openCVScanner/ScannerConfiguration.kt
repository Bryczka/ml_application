package pl.edu.wat.ml_application.openCVScanner

import team.clevel.documentscanner.helpers.ScannerConstants

class ScannerActivity {
    companion object {
        fun modify() {
            ScannerConstants.cropText = "Confirm"
            ScannerConstants.backText = "Cancel"
        }
    }
}