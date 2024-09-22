package eu.tutorials.wishlistapp

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import eu.tutorials.wishlistapp.data.Wish


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeView(
    navController: NavController,
    viewModel: WishViewModel
){

    Scaffold (
        topBar = { AppBarView(title = "WishList", onBackNavClicked = {}
        )},

        containerColor = colorResource(id = R.color.home_screen_color),

        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(24.dp),
                backgroundColor = Color.Black,
                onClick = {
                    navController.navigate(Screen.AddScreen.route + "/0L")
                 }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = Color.White)
            }
        }


    ) {
        val wishList = viewModel.getAllWishes.collectAsState(initial = listOf())
        LazyColumn(modifier = Modifier
            .fillMaxHeight()
            .padding(it)){
            items(wishList.value, key = {wish -> wish.id}){
                wish ->
                val dismissState = rememberDismissState(
                    confirmStateChange = {
                        if(it == DismissValue.DismissedToEnd || it == DismissValue.DismissedToStart){
                            viewModel.deleteWish(wish)
                        }
                        true
                    }
                )

                SwipeToDismiss(
                    state = dismissState,
                    background = {
                             val color by animateColorAsState(
                                 if(dismissState.dismissDirection == DismissDirection.EndToStart) Color.Red else Color.Transparent,
                                 label = ""
                             )
                        val alignment = Alignment.CenterEnd
                        Box (
                            Modifier
                                .fillMaxSize()
                                .background(color)
                                .padding(horizontal = 20.dp),
                            contentAlignment = alignment
                        ){
                            Icon(Icons.Default.Delete, contentDescription = "Delete Icon", tint = Color.White)
                        }
                    },
                    directions = setOf(DismissDirection.EndToStart),
                    dismissThresholds = {FractionalThreshold(0.5f)},
                    dismissContent = {
                        WishItem(wish = wish , onCLick = {
                            val id = wish.id
                            navController.navigate(Screen.AddScreen.route + "/$id") })
                    }
                )



            }
        }
    }

}

@Composable
fun WishItem(wish : Wish, onCLick: () -> Unit) {
    Card(modifier = Modifier
        .fillMaxSize()
        .padding(top = 8.dp, start = 8.dp, end = 8.dp)
        .clickable {
            onCLick()
        },
        elevation = 10.dp,
        backgroundColor = Color.White
    ){
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Text(text = wish.title, fontWeight = FontWeight.Bold, color = Color.Black)
            Text(text = wish.description, color = Color.Blue)
        }
    }
}
