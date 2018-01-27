shioharaApp.controller('ShContentListCtrl', [
		"$scope",
		"$http",
		"$window",
		"$state",
		"$stateParams",
		"$rootScope",
		"Token",
		"shUserResource",
		"shChannelResource",
		"shPostTypeResource",
		"shAPIServerService",
		'vigLocale',
		'$location',
		'$translate',
		function($scope, $http, $window, $state, $stateParams, $rootScope, Token,
				shUserResource, shChannelResource, shPostTypeResource, shAPIServerService, vigLocale, $location,
				$translate) {
			$scope.vigLanguage = vigLocale.getLocale().substring(0, 2);
			$translate.use($scope.vigLanguage);
			$scope.siteId = $stateParams.siteId;
			$scope.channelId = 0;
			$scope.accessToken = Token.get();
			$scope.shUser = null;
			$scope.shPosts = null;
			$scope.shLastPostType = null;
			$scope.shChannels = null;		
			$rootScope.$state = $state;
			$scope.breadcrumb = null;
			
			$scope.$evalAsync($http.get(
					shAPIServerService.get().concat(
							"/site/1/channel"))
					.then(function(response) {
						$scope.shChannels = response.data.shChannels;
						$scope.shPosts = response.data.shPosts;					
					}));
			
			$scope.shUser = shUserResource.get({
				id : 1,
				access_token : $scope.accessToken
			}, function() {
				$scope.shLastPostType = shPostTypeResource.get({
					id : $scope.shUser.lastPostType
				});
				
			});
			$scope.channelDelete = function(channelId) {
				shChannelResource
				.delete({
					id : channelId
				},function() {
					$state.go('content',{}, {reload: true});
				});
			}
		} ]);