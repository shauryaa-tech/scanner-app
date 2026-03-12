package com.smartlead.scanner.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.smartlead.scanner.presentation.screens.CameraScreen
import com.smartlead.scanner.presentation.screens.HomeScreen
import com.smartlead.scanner.presentation.screens.LeadsListScreen
import com.smartlead.scanner.presentation.screens.ResultScreen
import com.smartlead.scanner.presentation.viewmodel.LeadsViewModel
import com.smartlead.scanner.presentation.viewmodel.ScannerViewModel
import com.smartlead.scanner.presentation.viewmodel.ScrapViewModel
import com.smartlead.scanner.presentation.screens.ScrapDetailScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Camera : Screen("camera")
    object Result : Screen("result")
    object LeadsList : Screen("leads_list")
    object EditLead : Screen("edit_lead/{leadId}") {
        fun createRoute(leadId: Int) = "edit_lead/$leadId"
    }
    object LeadDetail : Screen("lead_detail/{leadId}?tab={tab}") {
        fun createRoute(leadId: Int, tab: Int = 0) = "lead_detail/$leadId?tab=$tab"
    }
    object ScrapDetail : Screen("scrap_detail/{leadId}") {
        fun createRoute(leadId: Int) = "scrap_detail/$leadId"
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val scannerViewModel: ScannerViewModel = hiltViewModel()
    
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToCamera = { navController.navigate(Screen.Camera.route) },
                onNavigateToLeads = { navController.navigate(Screen.LeadsList.route) },
                onImageSelected = { bitmap ->
                    scannerViewModel.processImage(bitmap)
                    navController.navigate(Screen.Result.route)
                }
            )
        }
        composable(Screen.Camera.route) {
            CameraScreen(
                onImageCaptured = { bitmap ->
                    scannerViewModel.processImage(bitmap)
                    navController.navigate(Screen.Result.route) {
                        popUpTo(Screen.Home.route)
                    }
                }
            )
        }
        composable(Screen.Result.route) {
            ResultScreen(
                viewModel = scannerViewModel,
                onSaveSuccess = {
                    navController.navigate(Screen.LeadsList.route) {
                        popUpTo(Screen.Home.route)
                    }
                },
                onRescan = {
                    navController.popBackStack()
                }
            )
        }
        composable(Screen.LeadsList.route) {
            val leadsViewModel: LeadsViewModel = hiltViewModel()
            LeadsListScreen(
                viewModel = leadsViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToEdit = { leadId ->
                    navController.navigate(Screen.EditLead.createRoute(leadId))
                },
                onLeadClick = { leadId ->
                    navController.navigate(com.smartlead.scanner.presentation.navigation.Screen.LeadDetail.createRoute(leadId, 0))
                },
                onScrapClick = { leadId ->
                    navController.navigate(com.smartlead.scanner.presentation.navigation.Screen.LeadDetail.createRoute(leadId, 1))
                }
            )
        }
        composable(
            route = Screen.EditLead.route,
            arguments = listOf(
                androidx.navigation.navArgument("leadId") {
                    type = androidx.navigation.NavType.IntType
                }
            )
        ) { backStackEntry ->
            val leadId = backStackEntry.arguments?.getInt("leadId") ?: 0
            com.smartlead.scanner.presentation.screens.EditLeadScreen(
                leadId = leadId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(
            route = Screen.LeadDetail.route,
            arguments = listOf(
                androidx.navigation.navArgument("leadId") {
                    type = androidx.navigation.NavType.IntType
                },
                androidx.navigation.navArgument("tab") {
                    type = androidx.navigation.NavType.IntType
                    defaultValue = 0
                }
            )
        ) { backStackEntry ->
            val leadId = backStackEntry.arguments?.getInt("leadId") ?: 0
            val initialTab = backStackEntry.arguments?.getInt("tab") ?: 0
            com.smartlead.scanner.presentation.screens.LeadDetailScreen(
                leadId = leadId,
                initialTab = initialTab,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToScrap = { id ->
                    navController.navigate(Screen.LeadDetail.createRoute(id, 1))
                }
            )
        }
        composable(
            route = Screen.ScrapDetail.route,
            arguments = listOf(
                androidx.navigation.navArgument("leadId") {
                    type = androidx.navigation.NavType.IntType
                }
            )
        ) { backStackEntry ->
            val leadId = backStackEntry.arguments?.getInt("leadId") ?: 0
            val scrapViewModel: ScrapViewModel = hiltViewModel()
            ScrapDetailScreen(
                leadId = leadId,
                viewModel = scrapViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
