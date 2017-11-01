# connatteo
###### /java/seedu/address/ui/MainWindow.java
``` java
    @Subscribe
    private void handleExportRequestEvent(ExportRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        handleExport();
    }
}
```
