#### create docker image
    './gradlew bootBuildImage --imageName=spring/jukebox'
#### run docker image
    'docker run -d -p 8080:8080 -t spring/jukebox'
    
### API 
    '/api/compatible/{setting_id}?model={model}&offset={offset}&limit={limit}'
    note: model, offset, and limit are all optional
    
### Error Codes
    200: {setting_id} exists
    404: {setting_id} does not exist