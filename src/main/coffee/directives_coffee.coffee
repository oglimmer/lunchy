LunchyApp = angular.module('LunchyApp');
  
LunchyApp.directive 'tagInput', ->
  restrict: 'E'
  scope: 
    inputTags: '=taglist'
    autocomplete: '=autocomplete'
    
  link: ($scope, element, attrs) ->
    
    $scope.defaultWidth = 200
    $scope.tagText = ''
    $scope.placeholder = attrs.placeholder
    
    if $scope.autocomplete
      
      $scope.autocompleteFocus = (event, ui) ->
        $(element).find('input').val( ui.item.value )
        return false

      $scope.autocompleteSelect = (event, ui) ->
        $scope.$apply "tagText='#{ui.item.value}'"
        $scope.$apply 'addTag()'
        return false
      
      $(element).find('input').autocomplete
        minLength: 0
        source: (request, response) ->
          response (item for item in $scope.autocomplete when item.toLowerCase().indexOf(request.term.toLowerCase()) != -1)
        focus: (event, ui) => $scope.autocompleteFocus(event, ui)
        select: (event, ui) => $scope.autocompleteSelect(event, ui)
    
    $scope.tagArray = ->
      return [] if $scope.inputTags is `undefined`
      $scope.inputTags.split(',').filter (tag) ->
        return tag != ""
    
    $scope.addTag = ->
      return if $scope.tagText.length is 0
      tagArray = $scope.tagArray()
      tagArray.push $scope.tagText
      $scope.inputTags = tagArray.join(',')
      $scope.tagText = ""
      
    $scope.deleteTag = (key) ->
      tagArray = $scope.tagArray()
      if tagArray.length > 0 and $scope.tagText.length is 0 and key is `undefined`
        tagArray.pop()
      else tagArray.splice key, 1  unless key is `undefined`
      $scope.inputTags = tagArray.join(',')
       
    # Watch for changes in text field
    $scope.$watch 'tagText', (newVal, oldVal) ->      
      unless newVal is oldVal and newVal is `undefined`        
        
        tempEl = $("<span>" + newVal + "</span>").appendTo("body")
        $scope.inputWidth = tempEl.width() + 5
        $scope.inputWidth = $scope.defaultWidth if $scope.inputWidth < $scope.defaultWidth
        tempEl.remove()        

    element.bind "keydown", (e) ->
      key = e.which
      
      e.preventDefault() if key is 9 or key is 13
      $scope.$apply 'deleteTag()' if key is 8

    element.bind "keyup", (e) ->
      key = e.which
  
      # Tab, Enter or , pressed 
      if key is 9 or key is 13 or key is 188
        e.preventDefault()
        $scope.$apply 'addTag()'

    $scope.focus = ->
      $(element).find('input').select()
      return true

  template: "<div class='tag-input-ctn form-control' style='height:auto !important' ng-click='focus()'><div class='input-tag' data-ng-repeat=\"tag in tagArray() track by $index\">{{tag}}<div class='delete-tag' data-ng-click='deleteTag($index)'>&times;</div></div><input type='text' autocomplete='off' data-ng-style='{width: inputWidth}' data-ng-model='tagText' placeholder='{{placeholder}}'/></div>"

